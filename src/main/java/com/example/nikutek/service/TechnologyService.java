package com.example.nikutek.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.nikutek.dto.TechnologyDTO;
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

@Service
@RequiredArgsConstructor
public class TechnologyService {

    private final TechnologyRepository technologyRepository;
    private final TechnologyTranslationRepository translationRepository;
    private final TechnologyCatalogRepository catalogRepository;
    private final LanguageRepository languageRepository;
    private final Cloudinary cloudinary;

    public List<TechnologyDTO> getAllTechnologies() {
        return technologyRepository.findAllOrdered()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // 🔸 Slug'a göre teknoloji çek (dil kodu ile)
    public TechnologyDTO getTechnologyBySlug(String slug, String langCode) {
        TechnologyTranslation translation = translationRepository.findBySlugAndLanguageCode(slug, langCode)
                .orElseThrow(() -> new RuntimeException("Teknoloji bulunamadı: " + slug + " (dil: " + langCode + ")"));
        
        return toDTO(translation.getTechnology());
    }

    public Technology addOrUpdateTechnology(Long id, boolean isActive, String imageUrl, String textContent) {
        Technology tech = id != null
                ? technologyRepository.findById(id).orElse(new Technology())
                : new Technology();

        tech.setActive(isActive);
        tech.setImageUrl(imageUrl);
        tech.setTextContent(textContent);

        return technologyRepository.save(tech);
    }

    @Transactional
    public void deleteTechnology(Long id) {
        Technology tech = technologyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Technology bulunamadı: " + id));

        translationRepository.deleteAll(translationRepository.findByTechnology(tech));
        catalogRepository.deleteAll(catalogRepository.findByTechnology(tech));
        technologyRepository.delete(tech);
    }

    public TechnologyTranslation addOrUpdateTranslation(Long technologyId, String langCode, String title, String description, String featuresDescription, String slug) {
        Technology tech = technologyRepository.findById(technologyId)
                .orElseThrow(() -> new RuntimeException("Technology bulunamadı: " + technologyId));

        Language language = languageRepository.findByCode(langCode)
                .orElseThrow(() -> new RuntimeException("Dil bulunamadı: " + langCode));

        TechnologyTranslation translation = translationRepository.findByTechnology(tech)
                .stream()
                .filter(t -> t.getLanguage().equals(language))
                .findFirst()
                .orElse(new TechnologyTranslation());

        translation.setTechnology(tech);
        translation.setLanguage(language);
        translation.setTitle(title);
        translation.setDescription(description);
        translation.setFeaturesDescription(featuresDescription);

        // Slug oluştur veya kullan
        if (slug == null || slug.trim().isEmpty()) {
            slug = SlugGenerator.generateSlug(title);
        }

        // Unique slug kontrolü
        Long excludeTranslationId = translation.getId();
        slug = SlugGenerator.ensureUniqueSlug(
            slug,
            s -> {
                Optional<TechnologyTranslation> existingOpt = translationRepository.findBySlug(s);
                if (existingOpt.isPresent()) {
                    TechnologyTranslation existing = existingOpt.get();
                    return excludeTranslationId == null || !existing.getId().equals(excludeTranslationId);
                }
                return false;
            },
            excludeTranslationId
        );

        translation.setSlug(slug);

        return translationRepository.save(translation);
    }

    public TechnologyCatalog addOrUpdateCatalog(Long technologyId, String name, String fileUrl) {
        Technology tech = technologyRepository.findById(technologyId)
                .orElseThrow(() -> new RuntimeException("Technology bulunamadı: " + technologyId));

        TechnologyCatalog catalog = catalogRepository.findByTechnology(tech)
                .stream()
                .filter(c -> c.getName().equals(name))
                .findFirst()
                .orElse(new TechnologyCatalog());

        catalog.setTechnology(tech);
        catalog.setName(name);
        catalog.setFileUrl(fileUrl);

        return catalogRepository.save(catalog);
    }

    // File upload - PDF'ler Cloudinary'ye yüklenir, backend proxy ile serve edilir
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
                            "folder", "nikutek/technologies",
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
                return "/api/nikutek/technologies/files/" + urlSafePublicId;
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
            // Örnek: nikutek_technologies_ciyltgjke8yhpflxguay -> nikutek/technologies/ciyltgjke8yhpflxguay
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

    // Sıralama Güncelle
    @Transactional
    public void reorderTechnologies(List<ReorderItem> items) {
        for (ReorderItem item : items) {
            Technology technology = technologyRepository.findById(item.getId())
                    .orElseThrow(() -> new RuntimeException("Technology bulunamadı: " + item.getId()));
            technology.setDisplayOrder(item.getDisplayOrder());
            technologyRepository.save(technology);
        }
    }

    @lombok.Data
    public static class ReorderItem {
        private Long id;
        private Integer displayOrder;
    }

    private TechnologyDTO toDTO(Technology entity) {
        TechnologyDTO dto = new TechnologyDTO();
        dto.setId(entity.getId());
        dto.setActive(entity.isActive());
        dto.setImageUrl(entity.getImageUrl());
        dto.setTextContent(entity.getTextContent());
        dto.setDisplayOrder(entity.getDisplayOrder());

        dto.setTranslations(translationRepository.findByTechnology(entity)
                .stream()
                .map(t -> {
                    TechnologyDTO.TechnologyTranslationDTO tdto = new TechnologyDTO.TechnologyTranslationDTO();
                    tdto.setLangCode(t.getLanguage().getCode());
                    tdto.setTitle(t.getTitle());
                    tdto.setDescription(t.getDescription());
                    tdto.setFeaturesDescription(t.getFeaturesDescription());
                    tdto.setSlug(t.getSlug());
                    return tdto;
                }).collect(Collectors.toList()));

        dto.setCatalogs(catalogRepository.findByTechnology(entity)
                .stream()
                .map(c -> {
                    TechnologyDTO.TechnologyCatalogDTO cdto = new TechnologyDTO.TechnologyCatalogDTO();
                    cdto.setName(c.getName());
                    cdto.setFileUrl(c.getFileUrl());
                    return cdto;
                }).collect(Collectors.toList()));

        return dto;
    }
}
