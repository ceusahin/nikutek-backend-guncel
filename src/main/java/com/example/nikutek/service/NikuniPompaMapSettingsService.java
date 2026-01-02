package com.example.nikutek.service;

import com.example.nikutek.entity.NikuniPompaMapSettings;
import com.example.nikutek.repository.NikuniPompaMapSettingsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NikuniPompaMapSettingsService {

    private final NikuniPompaMapSettingsRepository repository;

    public NikuniPompaMapSettingsService(NikuniPompaMapSettingsRepository repository) {
        this.repository = repository;
    }

    // Tek kayıt döndür (yoksa null)
    public NikuniPompaMapSettings getMapSettings() {
        List<NikuniPompaMapSettings> all = repository.findAll();
        return all.isEmpty() ? null : all.get(0);
    }

    // Yeni kayıt oluştur veya var olanı güncelle
    public NikuniPompaMapSettings saveOrUpdate(NikuniPompaMapSettings mapSettings) {
        List<NikuniPompaMapSettings> all = repository.findAll();
        if (all.isEmpty()) {
            // Hiç yoksa yeni oluştur
            mapSettings.setActive(true);
            return repository.save(mapSettings);
        } else {
            // Varsa mevcut kaydı güncelle
            NikuniPompaMapSettings existing = all.get(0);
            existing.setIframeUrl(mapSettings.getIframeUrl());
            existing.setActive(true);
            return repository.save(existing);
        }
    }

    // Haritayı sil (opsiyonel)
    public void deleteMapSettings() {
        repository.deleteAll();
    }
}

