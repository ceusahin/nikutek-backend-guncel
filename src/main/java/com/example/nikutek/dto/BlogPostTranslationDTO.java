package com.example.nikutek.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogPostTranslationDTO {
    private Long id;
    private String languageCode; // tr, en vb.
    private String title;
    private String description;
}