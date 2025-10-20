package com.example.nikutek.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "product_translation", schema = "nikutek")
@Data
public class ProductTranslation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;
}
