package com.example.nikutek.service;

import com.example.nikutek.entity.NikuniPompaSocialMediaSettings;
import com.example.nikutek.repository.NikuniPompaSocialMediaSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NikuniPompaSocialMediaSettingsService {

    @Autowired
    private NikuniPompaSocialMediaSettingsRepository repository;

    public List<NikuniPompaSocialMediaSettings> getAll() {
        return repository.findAll();
    }

    public NikuniPompaSocialMediaSettings update(String platform, boolean visible, String url) {
        NikuniPompaSocialMediaSettings setting = repository.findByPlatform(platform)
                .orElseThrow(() -> new RuntimeException("Platform not found"));

        setting.setVisible(visible);
        setting.setUrl(url);

        return repository.save(setting);
    }
}

