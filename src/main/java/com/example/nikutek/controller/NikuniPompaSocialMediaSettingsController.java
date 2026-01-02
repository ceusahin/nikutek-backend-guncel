package com.example.nikutek.controller;

import com.example.nikutek.entity.NikuniPompaSocialMediaSettings;
import com.example.nikutek.service.NikuniPompaSocialMediaSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nikuni-pompa/social-media")
public class NikuniPompaSocialMediaSettingsController {

    @Autowired
    private NikuniPompaSocialMediaSettingsService service;

    @GetMapping
    public List<NikuniPompaSocialMediaSettings> getAll() {
        return service.getAll();
    }

    @PutMapping("/{platform}")
    public NikuniPompaSocialMediaSettings updateSetting(
            @PathVariable String platform,
            @RequestParam boolean visible,
            @RequestParam String url
    ) {
        return service.update(platform, visible, url);
    }
}

