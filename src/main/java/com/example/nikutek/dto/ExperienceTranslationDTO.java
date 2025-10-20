package com.example.nikutek.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExperienceTranslationDTO {
    private String language;
    private String numberText;
    private String labelText;
}
