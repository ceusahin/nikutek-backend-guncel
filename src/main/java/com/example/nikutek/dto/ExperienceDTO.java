package com.example.nikutek.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExperienceDTO {
    private Long id;
    private boolean visible;
    private List<ExperienceTranslationDTO> translations;
}