package com.example.nikutek.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "technology_catalog", schema = "nikunipompa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NikuniPompaTechnologyCatalog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "technology_id")
    private NikuniPompaTechnology technology;

    private String name;
    private String fileUrl;
}

