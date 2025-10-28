package com.example.nikutek.controller;

import com.example.nikutek.dto.TechnologyDTO;
import com.example.nikutek.entity.*;
import com.example.nikutek.service.TechnologyService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/technologies")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TechnologyController {

    private final TechnologyService technologyService;

    @GetMapping
    public ResponseEntity<List<TechnologyDTO>> getAllTechnologies() {
        return ResponseEntity.ok(technologyService.getAllTechnologies());
    }

    @PostMapping
    public ResponseEntity<Technology> addOrUpdateTechnology(@RequestBody TechnologyRequest req) {
        return ResponseEntity.ok(technologyService.addOrUpdateTechnology(req.getId(), req.isActive(), req.getImageUrl()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTechnology(@PathVariable Long id) {
        technologyService.deleteTechnology(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/translation")
    public ResponseEntity<TechnologyTranslation> addOrUpdateTranslation(@RequestBody TranslationRequest req) {
        return ResponseEntity.ok(technologyService.addOrUpdateTranslation(req.getTechnologyId(), req.getLangCode(), req.getTitle(), req.getDescription()));
    }

    @PostMapping("/catalog")
    public ResponseEntity<TechnologyCatalog> addOrUpdateCatalog(@RequestBody CatalogRequest req) {
        return ResponseEntity.ok(technologyService.addOrUpdateCatalog(req.getTechnologyId(), req.getName(), req.getFileUrl()));
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(technologyService.uploadFile(file));
    }

    @Data
    public static class TechnologyRequest {
        private Long id;
        private boolean active;
        private String imageUrl;
    }

    @Data
    public static class TranslationRequest {
        private Long technologyId;
        private String langCode;
        private String title;
        private String description;
    }

    @Data
    public static class CatalogRequest {
        private Long technologyId;
        private String name;
        private String fileUrl;
    }
}
