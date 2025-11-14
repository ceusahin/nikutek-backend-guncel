package com.example.nikutek.service;

import com.example.nikutek.entity.PageSeoSettings;
import com.example.nikutek.repository.PageSeoSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PageSeoSettingsService {

    @Autowired
    private PageSeoSettingsRepository repository;

    public PageSeoSettings getSeoByPageTypeAndLanguage(String pageType, String language) {
        return repository.findByPageTypeAndLanguage(pageType, language)
                .orElse(null);
    }

    public List<PageSeoSettings> getAllSeoSettings() {
        return repository.findAll();
    }

    public PageSeoSettings saveOrUpdateSeo(PageSeoSettings seo) {
        Optional<PageSeoSettings> existing = repository.findByPageTypeAndLanguage(
                seo.getPageType(), seo.getLanguage());
        
        if (existing.isPresent()) {
            PageSeoSettings existingSeo = existing.get();
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

