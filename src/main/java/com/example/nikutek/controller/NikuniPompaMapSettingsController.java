package com.example.nikutek.controller;

import com.example.nikutek.entity.NikuniPompaMapSettings;
import com.example.nikutek.service.NikuniPompaMapSettingsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/nikuni-pompa/map-settings")
public class NikuniPompaMapSettingsController {

    private final NikuniPompaMapSettingsService service;

    public NikuniPompaMapSettingsController(NikuniPompaMapSettingsService service) {
        this.service = service;
    }

    // Tek map kaydını getir
    @GetMapping
    public ResponseEntity<NikuniPompaMapSettings> getMapSettings() {
        NikuniPompaMapSettings settings = service.getMapSettings();
        if (settings == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(settings);
    }

    // Tek kaydı oluştur veya güncelle
    @PostMapping
    public ResponseEntity<NikuniPompaMapSettings> saveOrUpdate(@RequestBody NikuniPompaMapSettings mapSettings) {
        NikuniPompaMapSettings saved = service.saveOrUpdate(mapSettings);
        return ResponseEntity.ok(saved);
    }

    // Silmek istersen (opsiyonel)
    @DeleteMapping
    public ResponseEntity<Void> deleteMapSettings() {
        service.deleteMapSettings();
        return ResponseEntity.noContent().build();
    }
}

