package com.example.nikutek.dto;

import lombok.Data;
import java.util.List;

@Data
public class TechnologyDTO {
    private Long id;
    private boolean active;
    private String imageUrl;
    private String textContent;
    private Integer displayOrder;
    private List<TechnologyTranslationDTO> translations;
    private List<TechnologyCatalogDTO> catalogs;

    @Data
    public static class TechnologyTranslationDTO {
        private String langCode;
        private String title;
        private String description;
        private String featuresDescription;
        private String slug;
    }

    @Data
    public static class TechnologyCatalogDTO {
        private Long id;
        private String name;
        private String fileUrl;
    }
}
