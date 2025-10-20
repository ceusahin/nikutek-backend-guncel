package com.example.nikutek.dto;

import com.example.nikutek.enums.InfoType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ContactInfoDTO {
    private Long id;
    private InfoType type;
    private Boolean isActive;
    private List<ContactInfoTranslationDTO> translations;
}