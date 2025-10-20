package com.example.nikutek.dto;

import lombok.Data;
import java.util.List;

@Data
public class FooterMenuDTO {
    private Long id;
    private Long languageId;
    private String title;
    private List<FooterMenuItemDTO> items;
}
