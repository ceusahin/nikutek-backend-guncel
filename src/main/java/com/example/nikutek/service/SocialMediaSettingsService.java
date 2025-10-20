package com.example.nikutek.service;

import com.example.nikutek.entity.SocialMediaSettings;
import com.example.nikutek.repository.SocialMediaSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SocialMediaSettingsService {

    @Autowired
    private SocialMediaSettingsRepository repository;

    public List<SocialMediaSettings> getAll() {
        return repository.findAll();
    }

    public SocialMediaSettings update(String platform, boolean visible, String url) {
        SocialMediaSettings setting = repository.findByPlatform(platform)
                .orElseThrow(() -> new RuntimeException("Platform not found"));

        setting.setVisible(visible);
        setting.setUrl(url);

        return repository.save(setting);
    }
}
