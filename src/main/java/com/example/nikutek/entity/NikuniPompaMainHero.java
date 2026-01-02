package com.example.nikutek.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "main_hero", schema = "nikunipompa")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NikuniPompaMainHero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String header;

    @Column(length = 500)
    private String paragraph;

    @Column(name = "button1_text")
    private String button1Text;

    @Column(name = "button1_url")
    private String button1Url;

    @Column(name = "button2_text")
    private String button2Text;

    @Column(name = "button2_url")
    private String button2Url;

    @Column(name = "language_id", nullable = false)
    private Long languageId;

    @Column(name = "image_url")
    private String imageUrl;
}

