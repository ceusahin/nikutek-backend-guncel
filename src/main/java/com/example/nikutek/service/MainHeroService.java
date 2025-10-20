package com.example.nikutek.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.nikutek.dto.MainHeroDTO;
import com.example.nikutek.entity.Language;
import com.example.nikutek.entity.MainHero;
import com.example.nikutek.repository.LanguageRepository;
import com.example.nikutek.repository.MainHeroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MainHeroService {

    private final MainHeroRepository mainHeroRepository;
    private final LanguageRepository languageRepository;
    private final Cloudinary cloudinary; // Cloudinary config

    public List<MainHeroDTO> getAllByLanguageCode(String code) {
        Language language = languageRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Dil bulunamadı: " + code));

        List<MainHero> heroes = mainHeroRepository.findByLanguageId(language.getId());
        return heroes.stream().map(this::toDto).collect(Collectors.toList());
    }

    public MainHeroDTO toDto(MainHero hero) {
        Language language = languageRepository.findById(hero.getLanguageId())
                .orElseThrow(() -> new RuntimeException("Dil bulunamadı id: " + hero.getLanguageId()));

        MainHeroDTO dto = new MainHeroDTO();
        dto.setId(hero.getId());
        dto.setHeader(hero.getHeader());
        dto.setParagraph(hero.getParagraph());
        dto.setButton1Text(hero.getButton1Text());
        dto.setButton1Url(hero.getButton1Url());
        dto.setButton2Text(hero.getButton2Text());
        dto.setButton2Url(hero.getButton2Url());
        dto.setLanguageCode(language.getCode());
        dto.setImageUrl(hero.getImageUrl());
        return dto;
    }

    public MainHero saveOrUpdate(Long id,
                                 String header,
                                 String paragraph,
                                 String button1Text,
                                 String button1Url,
                                 String button2Text,
                                 String button2Url,
                                 String languageCode,
                                 MultipartFile image) {

        Language language = languageRepository.findByCode(languageCode)
                .orElseThrow(() -> new RuntimeException("Dil bulunamadı: " + languageCode));

        MainHero hero = id != null ?
                mainHeroRepository.findById(id).orElse(new MainHero()) :
                new MainHero();

        hero.setHeader(header);
        hero.setParagraph(paragraph);
        hero.setButton1Text(button1Text);
        hero.setButton1Url(button1Url);
        hero.setButton2Text(button2Text);
        hero.setButton2Url(button2Url);
        hero.setLanguageId(language.getId());

        if (image != null && !image.isEmpty()) {
            try {
                Map uploadResult = cloudinary.uploader().upload(image.getBytes(),
                        ObjectUtils.asMap("folder", "nikutek/main_hero"));
                hero.setImageUrl((String) uploadResult.get("secure_url"));
            } catch (Exception e) {
                throw new RuntimeException("Resim yüklenirken hata oluştu", e);
            }
        }

        return mainHeroRepository.save(hero);
    }

    public void deleteImage(Long id) {
        MainHero hero = mainHeroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hero bulunamadı: " + id));
        hero.setImageUrl(null);
        mainHeroRepository.save(hero);
    }

}
