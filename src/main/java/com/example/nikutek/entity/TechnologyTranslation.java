package com.example.nikutek.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "technology_translation", schema = "nikutek")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TechnologyTranslation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "technology_id")
    private Technology technology;

    @ManyToOne
    @JoinColumn(name = "language_id")
    private Language language;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;
}
