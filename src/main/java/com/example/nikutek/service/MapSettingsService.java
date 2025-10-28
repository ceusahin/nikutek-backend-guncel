package com.example.nikutek.service;

import com.example.nikutek.entity.MapSettings;
import com.example.nikutek.repository.MapSettingsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MapSettingsService {

    private final MapSettingsRepository repository;

    public MapSettingsService(MapSettingsRepository repository) {
        this.repository = repository;
    }

    // Tek kayıt döndür (yoksa null)
    public MapSettings getMapSettings() {
        List<MapSettings> all = repository.findAll();
        return all.isEmpty() ? null : all.get(0);
    }

    // Yeni kayıt oluştur veya var olanı güncelle
    public MapSettings saveOrUpdate(MapSettings mapSettings) {
        List<MapSettings> all = repository.findAll();
        if (all.isEmpty()) {
            // Hiç yoksa yeni oluştur
            mapSettings.setActive(true);
            return repository.save(mapSettings);
        } else {
            // Varsa mevcut kaydı güncelle
            MapSettings existing = all.get(0);
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
