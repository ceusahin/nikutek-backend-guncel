package com.example.nikutek.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "about_section", schema = "nikunipompa")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class NikuniPompaAboutUs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String paragraph;

    @Column(name = "small_title_1")
    private String smallTitle1;

    @Column(name = "small_text_1", columnDefinition = "TEXT")
    @JsonProperty("smallText1")
    private String smallText1;

    @Column(name = "small_title_2")
    private String smallTitle2;

    @Column(name = "small_text_2", columnDefinition = "TEXT")
    @JsonProperty("smallText2")
    private String smallText2;

    @Column(name = "small_title_3")
    private String smallTitle3;

    @Column(name = "small_text_3", columnDefinition = "TEXT")
    @JsonProperty("smallText3")
    private String smallText3;

    @Column(name = "media_url")
    private String mediaUrl;

    @Column(name = "youtube_video_id")
    private String youtubeVideoUrl;
}

