package com.example.nikutek.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "experience_translation", schema = "nikutek")
public class ExperienceTranslation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "language_id")
    private Language language;

    @Column(name = "number_text")
    private String numberText;

    @Column(name = "label_text")
    private String labelText;

    @ManyToOne
    @JoinColumn(name = "experience_id")
    private Experience experience;
}
