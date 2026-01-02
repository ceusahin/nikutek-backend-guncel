package com.example.nikutek.controller;

import com.example.nikutek.entity.PageSeoSettings;
import com.example.nikutek.service.PageSeoSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nikutek/page-seo")
public class PageSeoSettingsController {

    @Autowired
    private PageSeoSettingsService service;

    @GetMapping("/{pageType}/{language}")
    public ResponseEntity<PageSeoSettings> getSeoByPageTypeAndLanguage(
            @PathVariable String pageType,
            @PathVariable String language) {
        PageSeoSettings seo = service.getSeoByPageTypeAndLanguage(pageType, language);
        if (seo != null) {
            return ResponseEntity.ok(seo);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<PageSeoSettings>> getAllSeoSettings() {
        return ResponseEntity.ok(service.getAllSeoSettings());
    }

    @PostMapping
    public ResponseEntity<PageSeoSettings> saveOrUpdateSeo(@RequestBody PageSeoSettings seo) {
        return ResponseEntity.ok(service.saveOrUpdateSeo(seo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeo(@PathVariable Long id) {
        service.deleteSeo(id);
        return ResponseEntity.noContent().build();
    }
}

