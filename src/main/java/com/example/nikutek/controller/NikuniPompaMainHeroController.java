package com.example.nikutek.controller;

import com.example.nikutek.dto.MainHeroDTO;
import com.example.nikutek.entity.NikuniPompaMainHero;
import com.example.nikutek.service.NikuniPompaMainHeroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/nikuni-pompa/main-hero")
@RequiredArgsConstructor
public class NikuniPompaMainHeroController {

    private final NikuniPompaMainHeroService mainHeroService;

    @GetMapping("/{code}")
    public ResponseEntity<List<MainHeroDTO>> getByLanguage(@PathVariable String code) {
        return ResponseEntity.ok(mainHeroService.getAllByLanguageCode(code));
    }

    @PostMapping
    public ResponseEntity<MainHeroDTO> save(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam("header") String header,
            @RequestParam("paragraph") String paragraph,
            @RequestParam("button1Text") String button1Text,
            @RequestParam("button1Url") String button1Url,
            @RequestParam("button2Text") String button2Text,
            @RequestParam("button2Url") String button2Url,
            @RequestParam("languageCode") String languageCode,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        NikuniPompaMainHero hero = mainHeroService.saveOrUpdate(id, header, paragraph,
                button1Text, button1Url, button2Text, button2Url, languageCode, image);
        return ResponseEntity.ok(mainHeroService.toDto(hero));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        mainHeroService.deleteImage(id);
        return ResponseEntity.noContent().build();
    }
}

