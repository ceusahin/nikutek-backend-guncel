package com.example.nikutek.service;

import com.example.nikutek.entity.SeoSettings;
import com.example.nikutek.repository.SeoSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeoSettingsService {

    @Autowired
    private SeoSettingsRepository repository;

    public SeoSettings getSeo() {
        return repository.findAll().stream().findFirst().orElse(null);
    }

    public SeoSettings updateSeo(SeoSettings updated) {
        SeoSettings existing = repository.findAll().stream().findFirst().orElse(null);
        if (existing != null) {
            existing.setTitle(updated.getTitle());
            existing.setDescription(updated.getDescription());
            existing.setKeywords(updated.getKeywords());
            return repository.save(existing);
        } else {
            return repository.save(updated);
        }
    }
}
