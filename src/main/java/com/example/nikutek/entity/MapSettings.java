package com.example.nikutek.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "map_settings", schema = "nikutek")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MapSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "iframeUrl", nullable = false, columnDefinition = "text")
    private String iframeUrl;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;
}