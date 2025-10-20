package com.example.nikutek.dto;

import lombok.Data;
import java.util.List;

@Data
public class ProductDTO {
    private Long id;
    private String imageUrl;
    private boolean active;
    private Long parentId;
    private boolean hasChildren;
    private int level;

    private List<ProductDTO> children;
    private List<ProductTranslationDTO> translations;
    private List<ProductFeatureDTO> features;
    private List<ProductCatalogDTO> catalogs;

    @Data
    public static class ProductTranslationDTO {
        private String langCode;
        private String title;
        private String description;
    }

    @Data
    public static class ProductFeatureDTO {
        private String langCode;
        private String name;
        private String value;
    }

    @Data
    public static class ProductCatalogDTO {
        private String name;
        private String fileUrl;
    }
}
