package com.example.nikutek.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "technology", schema = "nikutek")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Technology {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_active")
    private boolean active;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "display_order")
    private Integer displayOrder;

    @OneToMany(mappedBy = "technology", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TechnologyTranslation> translations;

    @OneToMany(mappedBy = "technology", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TechnologyCatalog> catalogs;
}
