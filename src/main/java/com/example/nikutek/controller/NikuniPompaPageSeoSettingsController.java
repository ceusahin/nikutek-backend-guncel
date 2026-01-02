package com.example.nikutek.controller;

import com.example.nikutek.entity.NikuniPompaPageSeoSettings;
import com.example.nikutek.service.NikuniPompaPageSeoSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nikuni-pompa/page-seo")
public class NikuniPompaPageSeoSettingsController {

    @Autowired
    private NikuniPompaPageSeoSettingsService service;

    @GetMapping("/{pageType}/{language}")
    public ResponseEntity<NikuniPompaPageSeoSettings> getSeoByPageTypeAndLanguage(
            @PathVariable String pageType,
            @PathVariable String language) {
        NikuniPompaPageSeoSettings seo = service.getSeoByPageTypeAndLanguage(pageType, language);
        if (seo != null) {
            return ResponseEntity.ok(seo);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<NikuniPompaPageSeoSettings>> getAllSeoSettings() {
        return ResponseEntity.ok(service.getAllSeoSettings());
    }

    @PostMapping
    public ResponseEntity<NikuniPompaPageSeoSettings> saveOrUpdateSeo(@RequestBody NikuniPompaPageSeoSettings seo) {
        return ResponseEntity.ok(service.saveOrUpdateSeo(seo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeo(@PathVariable Long id) {
        service.deleteSeo(id);
        return ResponseEntity.noContent().build();
    }
}

