package com.example.nikutek.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "product_feature", schema = "nikutek")
@Data
public class ProductFeature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;

    @Column(name = "feature_name", nullable = false)
    private String featureName;

    @Column(name = "feature_value", nullable = false)
    private String featureValue;

    @Column(name = "frequency")
    private Integer frequency;
}
