package com.example.nikutek.controller;

import com.example.nikutek.entity.SocialMediaSettings;
import com.example.nikutek.service.SocialMediaSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nikutek/social-media")
public class SocialMediaSettingsController {

    @Autowired
    private SocialMediaSettingsService service;

    @GetMapping
    public List<SocialMediaSettings> getAll() {
        return service.getAll();
    }

    @PutMapping("/{platform}")
    public SocialMediaSettings updateSetting(
            @PathVariable String platform,
            @RequestParam boolean visible,
            @RequestParam String url
    ) {
        return service.update(platform, visible, url);
    }
}