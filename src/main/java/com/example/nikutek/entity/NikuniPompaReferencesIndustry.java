package com.example.nikutek.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "references_industry", schema = "nikunipompa")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NikuniPompaReferencesIndustry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "industry", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NikuniPompaReferencesIndustryTranslation> translations;

    @OneToMany(mappedBy = "industry", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NikuniPompaReferencesCompany> companies;
}

