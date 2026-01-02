package com.example.nikutek.controller;

import com.example.nikutek.entity.NikuniPompaLogo;
import com.example.nikutek.service.NikuniPompaLogoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/nikuni-pompa/logo")
@RequiredArgsConstructor
public class NikuniPompaLogoController {

    private final NikuniPompaLogoService logoService;

    // Mevcut logoyu getir
    @GetMapping
    public ResponseEntity<NikuniPompaLogo> getLogo() {
        NikuniPompaLogo logo = logoService.getLogo();
        return ResponseEntity.ok(logo); // logo yoksa null döner
    }

    // Logo yükle / güncelle
    @PostMapping
    public ResponseEntity<NikuniPompaLogo> uploadLogo(@RequestParam("file") MultipartFile file) throws IOException {
        NikuniPompaLogo logo = logoService.uploadLogo(file);
        return ResponseEntity.ok(logo);
    }

    // Logo sil
    @DeleteMapping
    public ResponseEntity<Void> deleteLogo() throws IOException {
        logoService.deleteLogo();
        return ResponseEntity.noContent().build();
    }
}

