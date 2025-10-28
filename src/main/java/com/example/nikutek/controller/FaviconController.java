package com.example.nikutek.controller;

import com.example.nikutek.entity.Favicon;
import com.example.nikutek.repository.FaviconRepository;
import com.example.nikutek.service.FaviconService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/favicon")
@CrossOrigin(origins = "*")
public class FaviconController {

    private final FaviconService faviconService;

    public FaviconController(FaviconService faviconService) {
        this.faviconService = faviconService;
    }

    @GetMapping
    public ResponseEntity<Favicon> getFavicon() {
        Favicon favicon = faviconService.getFavicon();
        return ResponseEntity.ok(favicon); // tek obje döner
    }

    @PostMapping
    public ResponseEntity<Favicon> upload(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(faviconService.uploadFavicon(file));
    }

    @DeleteMapping
    public ResponseEntity<Void> delete() throws IOException {
        faviconService.deleteFavicon();
        return ResponseEntity.noContent().build();
    }
}


