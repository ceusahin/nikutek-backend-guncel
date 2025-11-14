package com.example.nikutek.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "technology_translation", schema = "nikutek")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TechnologyTranslation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "technology_id")
    private Technology technology;

    @ManyToOne
    @JoinColumn(name = "language_id")
    private Language language;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "features_description", columnDefinition = "TEXT")
    private String featuresDescription;

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
