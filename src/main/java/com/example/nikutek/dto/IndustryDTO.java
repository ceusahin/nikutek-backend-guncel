package com.example.nikutek.dto;

import lombok.Data;
import java.util.List;

@Data
public class IndustryDTO {
    private Long id;
    private String name;
    private List<CompanyDTO> companies;
}
