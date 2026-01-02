package com.example.nikutek.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "product_translation", schema = "nikunipompa")
@Data
public class NikuniPompaProductTranslation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private NikuniPompaProduct product;

    @ManyToOne
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @Column(name = "slug", unique = true)
    private String slug;
    
    @Column(name = "seo_title", length = 500)
    private String seoTitle;
    
    @Column(name = "seo_description", columnDefinition = "TEXT")
    private String seoDescription;
    
    @Column(name = "seo_keywords", columnDefinition = "TEXT")
    private String seoKeywords;
    
    @Column(name = "seo_og_title", length = 500)
    private String seoOgTitle;
    
    @Column(name = "seo_og_description", columnDefinition = "TEXT")
    private String seoOgDescription;
    
    @Column(name = "seo_og_image", length = 1000)
    private String seoOgImage;
}

