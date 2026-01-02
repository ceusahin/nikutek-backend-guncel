package com.example.nikutek.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "contact_info_translation", schema = "nikunipompa")
public class NikuniPompaContactInfoTranslation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "language_id")
    private Language language;

    private String title;
    private String content;

    @ManyToOne
    @JoinColumn(name = "contact_info_id")
    private NikuniPompaContactInfo contactInfo;
}

