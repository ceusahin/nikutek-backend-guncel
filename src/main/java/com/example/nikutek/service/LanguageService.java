package com.example.nikutek.service;

import com.example.nikutek.entity.Language;
import com.example.nikutek.repository.LanguageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LanguageService {

    private final LanguageRepository languageRepository;

    public List<Language> getAllLanguages() {
        return languageRepository.findAll();
    }

    public Language addLanguage(Language language) {
        if (languageRepository.existsByCode(language.getCode())) {
            throw new RuntimeException("Bu dil kodu zaten mevcut: " + language.getCode());
        }
        return languageRepository.save(language);
    }

    public void deleteLanguage(Long id) {
        languageRepository.deleteById(id);
    }
}
