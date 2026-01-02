package com.example.nikutek.controller;

import com.example.nikutek.entity.MapSettings;
import com.example.nikutek.service.MapSettingsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/nikutek/map-settings")
public class MapSettingsController {

    private final MapSettingsService service;

    public MapSettingsController(MapSettingsService service) {
        this.service = service;
    }

    // Tek map kaydını getir
    @GetMapping
    public ResponseEntity<MapSettings> getMapSettings() {
        MapSettings settings = service.getMapSettings();
        if (settings == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(settings);
    }

    // Tek kaydı oluştur veya güncelle
    @PostMapping
    public ResponseEntity<MapSettings> saveOrUpdate(@RequestBody MapSettings mapSettings) {
        MapSettings saved = service.saveOrUpdate(mapSettings);
        return ResponseEntity.ok(saved);
    }

    // Silmek istersen (opsiyonel)
    @DeleteMapping
    public ResponseEntity<Void> deleteMapSettings() {
        service.deleteMapSettings();
        return ResponseEntity.noContent().build();
    }
}
