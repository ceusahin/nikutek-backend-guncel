package com.example.nikutek.service;

import com.example.nikutek.entity.NikuniPompaAboutUs;
import com.example.nikutek.entity.Language;
import com.example.nikutek.repository.NikuniPompaAboutUsRepository;
import com.example.nikutek.repository.LanguageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NikuniPompaAboutUsService {

    private final NikuniPompaAboutUsRepository aboutUsRepository;
    private final LanguageRepository languageRepository;

    public List<NikuniPompaAboutUs> getAllByLanguageCode(String code) {
        Language lang = languageRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Dil bulunamadı: " + code));
        return aboutUsRepository.findByLanguage(lang);
    }

    public NikuniPompaAboutUs addOrUpdate(NikuniPompaAboutUs aboutUs, String languageCode) {
        Language lang = languageRepository.findByCode(languageCode)
                .orElseThrow(() -> new RuntimeException("Dil bulunamadı: " + languageCode));
        aboutUs.setLanguage(lang);
        return aboutUsRepository.save(aboutUs);
    }

    public void delete(Long id) {
        aboutUsRepository.deleteById(id);
    }
}

