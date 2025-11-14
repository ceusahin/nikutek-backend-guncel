package com.example.nikutek.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.nikutek.dto.TechnologyDTO;
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

    // Cloudinary upload
    public String uploadFile(MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "nikutek/technologies",
                            "overwrite", true,
                            "resource_type", "auto"
                    ));
            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Dosya yüklenemedi: " + e.getMessage());
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
