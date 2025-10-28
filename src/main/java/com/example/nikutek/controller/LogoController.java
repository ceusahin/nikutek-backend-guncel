package com.example.nikutek.controller;

import com.example.nikutek.entity.Logo;
import com.example.nikutek.service.LogoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/logo")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LogoController {

    private final LogoService logoService;

    // Mevcut logoyu getir
    @GetMapping
    public ResponseEntity<Logo> getLogo() {
        Logo logo = logoService.getLogo();
        return ResponseEntity.ok(logo); // logo yoksa null döner
    }

    // Logo yükle / güncelle
    @PostMapping
    public ResponseEntity<Logo> uploadLogo(@RequestParam("file") MultipartFile file) throws IOException {
        Logo logo = logoService.uploadLogo(file);
        return ResponseEntity.ok(logo);
    }

    // Logo sil
    @DeleteMapping
    public ResponseEntity<Void> deleteLogo() throws IOException {
        logoService.deleteLogo();
        return ResponseEntity.noContent().build();
    }
}
