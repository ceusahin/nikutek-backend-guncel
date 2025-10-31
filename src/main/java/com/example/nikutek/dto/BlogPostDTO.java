package com.example.nikutek.dto;

import com.example.nikutek.enums.BlogPostType;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogPostDTO {
    private Long id;
    private BlogPostType type;
    private String videoLink;
    private Boolean active;
    private Integer displayOrder;
    private List<BlogPostTranslationDTO> translations;
    private List<BlogPostImageDTO> images;
}