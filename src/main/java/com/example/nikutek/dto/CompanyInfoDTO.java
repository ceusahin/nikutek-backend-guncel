package com.example.nikutek.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyInfoDTO {
    private Long id;
    private String companyName;
    private String companyDescription;
    private String languageCode; // 'tr' veya 'en'
}
