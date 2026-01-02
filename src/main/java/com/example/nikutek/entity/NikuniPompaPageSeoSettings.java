package com.example.nikutek.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "page_seo_settings", schema = "nikunipompa", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"page_type", "language"})
})
public class NikuniPompaPageSeoSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "page_type", nullable = false, length = 50)
    private String pageType; // home, about, technologies, technology_detail, products, product_detail, category_products, blog, references, contact

    @Column(name = "language", nullable = false, length = 10)
    private String language; // tr, en

    @Column(name = "title", length = 500)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "keywords", columnDefinition = "TEXT")
    private String keywords;

    @Column(name = "og_title", length = 500)
    private String ogTitle;

    @Column(name = "og_description", columnDefinition = "TEXT")
    private String ogDescription;

    @Column(name = "og_image", length = 1000)
    private String ogImage;
}

