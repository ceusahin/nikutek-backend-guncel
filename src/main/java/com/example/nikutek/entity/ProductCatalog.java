package com.example.nikutek.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "product_catalog", schema = "nikutek")
@Data
public class ProductCatalog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private String name;
    private String fileUrl;
}
