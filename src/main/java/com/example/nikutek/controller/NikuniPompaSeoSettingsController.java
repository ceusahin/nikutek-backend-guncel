package com.example.nikutek.controller;

import com.example.nikutek.entity.NikuniPompaSeoSettings;
import com.example.nikutek.service.NikuniPompaSeoSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/nikuni-pompa/seo")
public class NikuniPompaSeoSettingsController {

    @Autowired
    private NikuniPompaSeoSettingsService service;

    @GetMapping
    public NikuniPompaSeoSettings getSeo() {
        return service.getSeo();
    }

    @PutMapping
    public NikuniPompaSeoSettings updateSeo(@RequestBody NikuniPompaSeoSettings seo) {
        return service.updateSeo(seo);
    }
}

