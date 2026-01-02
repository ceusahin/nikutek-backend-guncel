package com.example.nikutek.service;

import com.example.nikutek.entity.NikuniPompaPageSeoSettings;
import com.example.nikutek.repository.NikuniPompaPageSeoSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NikuniPompaPageSeoSettingsService {

    @Autowired
    private NikuniPompaPageSeoSettingsRepository repository;

    public NikuniPompaPageSeoSettings getSeoByPageTypeAndLanguage(String pageType, String language) {
        return repository.findByPageTypeAndLanguage(pageType, language)
                .orElse(null);
    }

    public List<NikuniPompaPageSeoSettings> getAllSeoSettings() {
        return repository.findAll();
    }

    public NikuniPompaPageSeoSettings saveOrUpdateSeo(NikuniPompaPageSeoSettings seo) {
        Optional<NikuniPompaPageSeoSettings> existing = repository.findByPageTypeAndLanguage(
                seo.getPageType(), seo.getLanguage());
        
        if (existing.isPresent()) {
            NikuniPompaPageSeoSettings existingSeo = existing.get();
            existingSeo.setTitle(seo.getTitle());
            existingSeo.setDescription(seo.getDescription());
            existingSeo.setKeywords(seo.getKeywords());
            existingSeo.setOgTitle(seo.getOgTitle());
            existingSeo.setOgDescription(seo.getOgDescription());
            existingSeo.setOgImage(seo.getOgImage());
            return repository.save(existingSeo);
        } else {
            return repository.save(seo);
        }
    }

    public void deleteSeo(Long id) {
        repository.deleteById(id);
    }
}

