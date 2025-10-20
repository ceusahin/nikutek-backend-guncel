package com.example.nikutek.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "references_company", schema = "nikutek")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReferencesCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "industry_id", nullable = false)
    private ReferencesIndustry industry;

    @Column(nullable = false)
    private String name;
}
