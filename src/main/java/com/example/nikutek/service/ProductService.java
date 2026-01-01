package com.example.nikutek.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.nikutek.dto.ProductDTO;
import com.example.nikutek.entity.*;
import com.example.nikutek.repository.*;
import com.example.nikutek.utils.SlugGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Data;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductTranslationRepository translationRepository;
    private final ProductFeatureRepository featureRepository;
    private final ProductCatalogRepository catalogRepository;
    private final LanguageRepository languageRepository;
    private final Cloudinary cloudinary;

    // 🔸 Tüm ürünleri çek (ana + alt) - displayOrder'e göre sıralı
    public List<ProductDTO> getAllProducts() {
        return productRepository.findParentProductsOrdered()
                .stream()
                .map(p -> toDTO(p, 0))
                .toList();
    }

    // 🔸 ID'ye göre ürün çek
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı: " + id));
        return toDTO(product, 0);
    }

    // 🔸 Slug'a göre ürün çek (dil kodu ile)
    public ProductDTO getProductBySlug(String slug, String langCode) {
        Language language = languageRepository.findByCode(langCode)
                .orElseThrow(() -> new RuntimeException("Dil bulunamadı: " + langCode));
        
        ProductTranslation translation = translationRepository.findBySlugAndLanguage(slug, language)
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı: " + slug));
        
        return toDTO(translation.getProduct(), 0);
    }

    // 🔸 Ürün kaydet / güncelle (tam model)
    @Transactional
    public ProductDTO saveOrUpdateFullProduct(ProductDTO dto) {
        Product product = saveOrUpdateSingleProduct(dto, null);
        return toDTO(product, 0);
    }

    // Tek bir ürün kaydet / güncelle (parentId ile birlikte)
    private Product saveOrUpdateSingleProduct(ProductDTO dto, Product parent) {
        Product product = (dto.getId() != null)
                ? productRepository.findById(dto.getId()).orElse(new Product())
                : new Product();

        // Parent ilişkisi - DTO'dan parentId'yi al ve parent'ı bul
        Long newParentId = (dto.getParentId() != null) ? dto.getParentId() : 
                           (parent != null) ? parent.getId() : null;
        
        // Circular reference kontrolü
        if (product.getId() != null && newParentId != null) {
            validateParentId(product.getId(), newParentId);
        }
        
        if (dto.getParentId() != null) {
            Product parentProduct = productRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent ürün bulunamadı: " + dto.getParentId()));
            product.setParent(parentProduct);
        } else if (parent != null) {
            // Eğer parent parametresi varsa (children için) onu kullan
            product.setParent(parent);
        } else {
            // Hiçbiri yoksa null set et (ana ürün yap)
            product.setParent(null);
        }

        product.setImageUrl(dto.getImageUrl());
        product.setActive(dto.isActive());

        product = productRepository.save(product);

        // 🔹 Translations
        // Güncelleme durumunda mevcut translation'ları silme, sadece ilgili olanları güncelle
        if (dto.getId() != null) {
            // Mevcut translation'ları sil
            translationRepository.deleteByProduct(product);
        }
        
        if (dto.getTranslations() != null) {
            for (var t : dto.getTranslations()) {
                Language lang = languageRepository.findByCode(t.getLangCode())
                        .orElseThrow(() -> new RuntimeException("Dil bulunamadı: " + t.getLangCode()));

                ProductTranslation trans = new ProductTranslation();
                trans.setProduct(product);
                trans.setLanguage(lang);
                trans.setTitle(t.getTitle());
                trans.setDescription(t.getDescription());
                
                // SEO alanları
                trans.setSeoTitle(t.getSeoTitle());
                trans.setSeoDescription(t.getSeoDescription());
                trans.setSeoKeywords(t.getSeoKeywords());
                trans.setSeoOgTitle(t.getSeoOgTitle());
                trans.setSeoOgDescription(t.getSeoOgDescription());
                trans.setSeoOgImage(t.getSeoOgImage());
                
                // Slug oluştur veya kullan (eğer DTO'da varsa)
                String slug = t.getSlug();
                if (slug == null || slug.trim().isEmpty()) {
                    // Otomatik slug oluştur
                    slug = SlugGenerator.generateSlug(t.getTitle());
                }
                
                // Unique slug kontrolü
                Long excludeTranslationId = (dto.getId() != null) ? 
                    translationRepository.findByProductAndLanguage(product, lang)
                        .map(ProductTranslation::getId).orElse(null) : null;
                
                slug = SlugGenerator.ensureUniqueSlug(
                    slug,
                    s -> {
                        Optional<ProductTranslation> existingOpt = translationRepository.findBySlug(s);
                        if (existingOpt.isPresent()) {
                            ProductTranslation existing = existingOpt.get();
                            return excludeTranslationId == null || !existing.getId().equals(excludeTranslationId);
                        }
                        return false;
                    },
                    excludeTranslationId
                );
                
                trans.setSlug(slug);
                translationRepository.save(trans);
            }
        }

        // 🔹 Features
        featureRepository.deleteByProduct(product);
        if (dto.getFeatures() != null) {
            for (var f : dto.getFeatures()) {
                Language lang = languageRepository.findByCode(f.getLangCode())
                        .orElseThrow(() -> new RuntimeException("Dil bulunamadı: " + f.getLangCode()));

                ProductFeature feature = new ProductFeature();
                feature.setProduct(product);
                feature.setLanguage(lang);
                feature.setFeatureName(f.getName());
                feature.setFeatureValue(f.getValue());
                feature.setFrequency(f.getFrequency());
                featureRepository.save(feature);
            }
        }

        // 🔹 Catalogs
        catalogRepository.deleteByProduct(product);
        if (dto.getCatalogs() != null) {
            for (var c : dto.getCatalogs()) {
                ProductCatalog catalog = new ProductCatalog();
                catalog.setProduct(product);
                catalog.setName(c.getName());
                catalog.setFileUrl(c.getFileUrl());
                catalogRepository.save(catalog);
            }
        }

        // 🔹 Children (recursive)
        if (dto.getChildren() != null) {
            for (var childDto : dto.getChildren()) {
                saveOrUpdateSingleProduct(childDto, product);
            }
        }

        return product;
    }


    // 🔸 Ürün sil (çocuklarıyla birlikte)
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı: " + id));

        // Alt ürünleri de sil
        for (Product child : product.getChildren()) {
            deleteProduct(child.getId());
        }

        translationRepository.deleteByProduct(product);
        featureRepository.deleteByProduct(product);
        catalogRepository.deleteByProduct(product);

        productRepository.delete(product);
    }

    // 🔸 Toggle active (DTO dön)
    public ProductDTO toggleActiveDTO(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı: " + id));
        product.setActive(!product.isActive());
        productRepository.save(product);
        return toDTO(product, 0);
    }

    // 🔸 File upload - PDF'ler Cloudinary'ye yüklenir, backend proxy ile serve edilir
    public String uploadFile(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            if (fileName == null) {
                throw new RuntimeException("Dosya adı bulunamadı");
            }
            
            boolean isPdf = fileName.toLowerCase().endsWith(".pdf") || 
                           (file.getContentType() != null && file.getContentType().equals("application/pdf"));
            
            // PDF dosyaları için raw resource type kullan
            String resourceType = isPdf ? "raw" : "auto";
            
            // Cloudinary'ye yükle
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "nikutek/products",
                            "overwrite", true,
                            "resource_type", resourceType,
                            "access_mode", "public"
                    ));
            
            String cloudinaryUrl = uploadResult.get("secure_url").toString();
            
            // PDF ise, backend proxy URL'ini döndür (doğru headers ile serve edilsin)
            if (isPdf) {
                // Cloudinary'den direkt public_id'yi al (daha güvenilir)
                String publicId = uploadResult.get("public_id").toString();
                // public_id'yi URL-safe hale getir (slash'ları underscore'a çevir)
                String urlSafePublicId = publicId.replace("/", "_");
                System.out.println("PDF Upload - public_id: " + publicId);
                System.out.println("PDF Upload - urlSafePublicId: " + urlSafePublicId);
                return "/api/nikutek/products/files/" + urlSafePublicId;
            }
            
            // Resimler için direkt Cloudinary URL'ini döndür
            return cloudinaryUrl;
        } catch (IOException e) {
            throw new RuntimeException("Dosya yüklenemedi: " + e.getMessage());
        }
    }
    
    // Cloudinary URL'inden public_id'yi çıkar
    private String extractPublicIdFromUrl(String url) {
        try {
            // URL formatı: https://res.cloudinary.com/account/raw/upload/v123/folder/file.pdf
            // veya: https://res.cloudinary.com/account/raw/upload/folder/file.pdf
            System.out.println("Extracting public_id from URL: " + url);
            
            int uploadIndex = url.indexOf("/upload/");
            if (uploadIndex == -1) {
                System.err.println("Could not find /upload/ in URL");
                return UUID.randomUUID().toString();
            }
            
            String afterUpload = url.substring(uploadIndex + "/upload/".length());
            System.out.println("After /upload/: " + afterUpload);
            
            // v123 kısmını atla (version varsa)
            if (afterUpload.startsWith("v")) {
                int slashIndex = afterUpload.indexOf("/");
                if (slashIndex != -1) {
                    afterUpload = afterUpload.substring(slashIndex + 1);
                } else {
                    // Version var ama slash yok, sadece version varsa
                    return UUID.randomUUID().toString();
                }
            }
            
            System.out.println("After version: " + afterUpload);
            
            // Uzantıyı kaldır
            int dotIndex = afterUpload.lastIndexOf(".");
            if (dotIndex != -1) {
                afterUpload = afterUpload.substring(0, dotIndex);
            }
            
            System.out.println("After removing extension: " + afterUpload);
            
            // / karakterlerini _ ile değiştir (URL-safe)
            String publicId = afterUpload.replace("/", "_");
            System.out.println("Final public_id: " + publicId);
            return publicId;
        } catch (Exception e) {
            System.err.println("Error extracting public_id: " + e.getMessage());
            e.printStackTrace();
            return UUID.randomUUID().toString();
        }
    }
    
    // PDF dosyasını Cloudinary'den oku ve serve et
    public byte[] getPdfFile(String urlSafePublicId) throws IOException {
        try {
            // URL-safe public_id'yi geri çevir (folder/file formatına)
            // Örnek: nikutek_products_ciyltgjke8yhpflxguay -> nikutek/products/ciyltgjke8yhpflxguay
            String cloudinaryPublicId = urlSafePublicId.replace("_", "/");
            
            System.out.println("PDF Request - urlSafePublicId: " + urlSafePublicId);
            System.out.println("PDF Request - cloudinaryPublicId: " + cloudinaryPublicId);
            
            // Cloudinary'den PDF'i indir - secure URL kullan, format belirtme (Cloudinary otomatik anlar)
            String url = cloudinary.url()
                    .resourceType("raw")
                    .secure(true)
                    .generate(cloudinaryPublicId);
            
            System.out.println("PDF Request - Cloudinary URL: " + url);
            
            // URL'den dosyayı indir
            java.net.HttpURLConnection connection = (java.net.HttpURLConnection) new java.net.URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(30000);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            
            int responseCode = connection.getResponseCode();
            System.out.println("PDF Request - Response Code: " + responseCode);
            
            if (responseCode != 200) {
                String errorMessage = "Cloudinary'den PDF alınamadı. Response Code: " + responseCode;
                try (java.io.InputStream errorStream = connection.getErrorStream()) {
                    if (errorStream != null) {
                        String errorBody = new String(errorStream.readAllBytes());
                        errorMessage += " - Error: " + errorBody;
                        System.err.println(errorMessage);
                    }
                }
                throw new IOException(errorMessage);
            }
            
            try (java.io.InputStream in = connection.getInputStream()) {
                byte[] data = in.readAllBytes();
                System.out.println("PDF Request - File size: " + data.length + " bytes");
                if (data.length == 0) {
                    throw new IOException("PDF dosyası boş");
                }
                return data;
            }
        } catch (java.net.SocketTimeoutException e) {
            System.err.println("PDF Timeout Error: " + e.getMessage());
            throw new IOException("PDF Cloudinary'den alınırken zaman aşımı oluştu: " + e.getMessage(), e);
        } catch (java.io.FileNotFoundException e) {
            System.err.println("PDF File Not Found: " + e.getMessage());
            throw new IOException("PDF Cloudinary'de bulunamadı. Public ID: " + urlSafePublicId, e);
        } catch (Exception e) {
            System.err.println("PDF Error: " + e.getMessage());
            e.printStackTrace();
            throw new IOException("PDF Cloudinary'den alınamadı: " + e.getMessage(), e);
        }
    }

    // 🔸 Sıralama güncelle (sadece parent ürünler için)
    @Transactional
    public void reorderProducts(List<ReorderItem> items) {
        for (ReorderItem item : items) {
            Product product = productRepository.findById(item.getId())
                    .orElseThrow(() -> new RuntimeException("Ürün bulunamadı: " + item.getId()));
            
            // Güvenlik: Sadece parent ürünleri güncelle
            if (product.getParent() == null) {
                product.setDisplayOrder(item.getDisplayOrder());
                productRepository.save(product);
            }
        }
    }

    @Data
    public static class ReorderItem {
        private Long id;
        private Integer displayOrder;
    }

    // 🔸 Circular reference kontrolü
    private void validateParentId(Long productId, Long newParentId) {
        // Bir ürün kendi parent'ı olamaz
        if (productId.equals(newParentId)) {
            throw new RuntimeException("Bir ürün kendi parent'ı olamaz");
        }
        
        // Yeni parent'ın, mevcut ürünün alt ürünü olup olmadığını kontrol et (circular reference)
        if (isDescendantOf(productId, newParentId)) {
            throw new RuntimeException("Circular reference: Bu ürün seçilen parent'ın alt ürünü. Bir ürün kendi alt ürününün alt ürünü olamaz.");
        }
    }
    
    // Bir ürünün, başka bir ürünün alt ürünü (descendant) olup olmadığını kontrol et
    private boolean isDescendantOf(Long ancestorId, Long descendantId) {
        if (ancestorId == null || descendantId == null) {
            return false;
        }
        
        Product current = productRepository.findById(descendantId).orElse(null);
        int depth = 0;
        final int MAX_DEPTH = 100; // Sonsuz döngü önleme
        
        while (current != null && current.getParent() != null && depth < MAX_DEPTH) {
            if (current.getParent().getId().equals(ancestorId)) {
                return true;
            }
            current = current.getParent();
            depth++;
        }
        return false;
    }

    // 🔸 DTO dönüşümü
    private ProductDTO toDTO(Product product, int level) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setImageUrl(product.getImageUrl());
        dto.setActive(product.isActive());
        dto.setParentId(product.getParent() != null ? product.getParent().getId() : null);
        dto.setLevel(level);
        dto.setHasChildren(!product.getChildren().isEmpty());
        dto.setDisplayOrder(product.getDisplayOrder());

        // Translations
        dto.setTranslations(
                translationRepository.findByProduct(product)
                        .stream()
                        .map(t -> {
                            ProductDTO.ProductTranslationDTO tDto = new ProductDTO.ProductTranslationDTO();
                            tDto.setLangCode(t.getLanguage().getCode());
                            tDto.setTitle(t.getTitle());
                            tDto.setDescription(t.getDescription());
                            tDto.setSlug(t.getSlug());
                            tDto.setSeoTitle(t.getSeoTitle());
                            tDto.setSeoDescription(t.getSeoDescription());
                            tDto.setSeoKeywords(t.getSeoKeywords());
                            tDto.setSeoOgTitle(t.getSeoOgTitle());
                            tDto.setSeoOgDescription(t.getSeoOgDescription());
                            tDto.setSeoOgImage(t.getSeoOgImage());
                            return tDto;
                        }).toList()
        );

        // Features
        dto.setFeatures(
                featureRepository.findByProduct(product)
                        .stream()
                        .map(f -> {
                            ProductDTO.ProductFeatureDTO fDto = new ProductDTO.ProductFeatureDTO();
                            fDto.setLangCode(f.getLanguage().getCode());
                            fDto.setName(f.getFeatureName());
                            fDto.setValue(f.getFeatureValue());
                            fDto.setFrequency(f.getFrequency());
                            return fDto;
                        }).toList()
        );

        // Catalogs
        dto.setCatalogs(
                catalogRepository.findByProduct(product)
                        .stream()
                        .map(c -> {
                            ProductDTO.ProductCatalogDTO cDto = new ProductDTO.ProductCatalogDTO();
                            cDto.setName(c.getName());
                            cDto.setFileUrl(c.getFileUrl());
                            return cDto;
                        }).toList()
        );

        // Children
        dto.setChildren(product.getChildren()
                .stream()
                .map(child -> toDTO(child, level + 1))
                .toList());

        return dto;
    }
}

