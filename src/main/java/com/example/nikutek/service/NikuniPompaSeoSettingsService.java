package com.example.nikutek.service;

import com.example.nikutek.entity.NikuniPompaSeoSettings;
import com.example.nikutek.repository.NikuniPompaSeoSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NikuniPompaSeoSettingsService {

    @Autowired
    private NikuniPompaSeoSettingsRepository repository;

    public NikuniPompaSeoSettings getSeo() {
        return repository.findAll().stream().findFirst().orElse(null);
    }

    public NikuniPompaSeoSettings updateSeo(NikuniPompaSeoSettings updated) {
        NikuniPompaSeoSettings existing = repository.findAll().stream().findFirst().orElse(null);
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

