package com.example.nikutek.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ContactInfoTranslationDTO {
    private String languageCode;
    private String title;
    private String content;
}
