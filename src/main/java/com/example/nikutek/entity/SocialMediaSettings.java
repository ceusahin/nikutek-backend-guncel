package com.example.nikutek.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "social_media", schema = "nikutek")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SocialMediaSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, name = "platform")
    private String platform;

    @Column(name = "visible")
    private boolean visible;

    @Column(name = "url")
    private String url;
}
