package com.example.nikutek.dto;

import lombok.Data;

@Data
public class MainHeroDTO {
    private Long id;
    private String header;
    private String paragraph;
    private String button1Text;
    private String button1Url;
    private String button2Text;
    private String button2Url;
    private String languageCode;
    private String imageUrl;
}
