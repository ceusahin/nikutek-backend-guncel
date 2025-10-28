package com.example.nikutek.controller;

import com.example.nikutek.entity.SeoSettings;
import com.example.nikutek.service.SeoSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seo")
@CrossOrigin(origins = "*")
public class SeoSettingsController {

    @Autowired
    private SeoSettingsService service;

    @GetMapping
    public SeoSettings getSeo() {
        return service.getSeo();
    }

    @PutMapping
    public SeoSettings updateSeo(@RequestBody SeoSettings seo) {
        return service.updateSeo(seo);
    }
}