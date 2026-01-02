package com.example.nikutek.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "stock_log", schema = "nikunipompa")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NikuniPompaStockLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Silinen stock ile referans zorunlu değil, sadece id saklayacağız
    private Long stockId;

    private String actionType;
    private Integer oldAntrepoQuantity;
    private Integer newAntrepoQuantity;
    private Integer oldShopQuantity;
    private Integer newShopQuantity;
    private LocalDateTime actionTime;
    private String description;
}

