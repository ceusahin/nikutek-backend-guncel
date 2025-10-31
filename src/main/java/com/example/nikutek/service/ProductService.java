package com.example.nikutek.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.nikutek.dto.ProductDTO;
import com.example.nikutek.entity.*;
import com.example.nikutek.repository.*;
import com.example.nikutek.utils.SlugGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
        if (dto.getParentId() != null) {
            Product parentProduct = productRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent ürün bulunamadı: " + dto.getParentId()));
            product.setParent(parentProduct);
        } else if (parent != null) {
            // Eğer parent parametresi varsa (children için) onu kullan
            product.setParent(parent);
        } else {
            // Hiçbiri yoksa null set et
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

    // 🔸 Cloudinary upload
    public String uploadFile(MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "nikutek/products",
                            "overwrite", true,
                            "resource_type", "auto",
                            "access_mode", "public",
                            "flags", "attachment:false" // 👈 PDF'yi açılabilir hale getirir
                    ));
            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Dosya yüklenemedi: " + e.getMessage());
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

