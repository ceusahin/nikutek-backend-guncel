package com.example.nikutek.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "references_company", schema = "nikunipompa")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NikuniPompaReferencesCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "industry_id", nullable = false)
    private NikuniPompaReferencesIndustry industry;

    @Column(nullable = false)
    private String name;
}

