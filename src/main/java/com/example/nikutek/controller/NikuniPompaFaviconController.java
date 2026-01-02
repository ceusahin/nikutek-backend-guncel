package com.example.nikutek.controller;

import com.example.nikutek.entity.NikuniPompaFavicon;
import com.example.nikutek.service.NikuniPompaFaviconService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/nikuni-pompa/favicon")
public class NikuniPompaFaviconController {

    private final NikuniPompaFaviconService faviconService;

    public NikuniPompaFaviconController(NikuniPompaFaviconService faviconService) {
        this.faviconService = faviconService;
    }

    @GetMapping
    public ResponseEntity<NikuniPompaFavicon> getFavicon() {
        NikuniPompaFavicon favicon = faviconService.getFavicon();
        return ResponseEntity.ok(favicon); // tek obje döner
    }

    @PostMapping
    public ResponseEntity<NikuniPompaFavicon> upload(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(faviconService.uploadFavicon(file));
    }

    @DeleteMapping
    public ResponseEntity<Void> delete() throws IOException {
        faviconService.deleteFavicon();
        return ResponseEntity.noContent().build();
    }
}

