package com.example.nikutek.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogPostImageDTO {
    private Long id;
    private String imageUrl;
    private String altText;
    private Integer sortOrder;
}
