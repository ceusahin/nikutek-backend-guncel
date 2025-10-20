package com.example.nikutek.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "stock", schema = "nikutek")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productCode;
    private String productName;
    private String model;
    private Integer antrepoQuantity;
    private Integer shopQuantity;
    private LocalDateTime lastUpdate;
    private String note;

    @Transient
    private Integer oldAntrepoQuantity;

    @Transient
    private Integer oldShopQuantity;
}
