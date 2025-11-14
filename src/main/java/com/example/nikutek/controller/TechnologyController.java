package com.example.nikutek.controller;

import com.example.nikutek.dto.TechnologyDTO;
import com.example.nikutek.entity.*;
import com.example.nikutek.service.TechnologyService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/technologies")
@RequiredArgsConstructor
public class TechnologyController {

    private final TechnologyService technologyService;

    @GetMapping
    public ResponseEntity<List<TechnologyDTO>> getAllTechnologies() {
        return ResponseEntity.ok(technologyService.getAllTechnologies());
    }

    // 🔸 Slug'a göre teknoloji çek (SEO-friendly URL)
    @GetMapping("/slug/{slug}")
    public ResponseEntity<TechnologyDTO> getTechnologyBySlug(
            @PathVariable String slug,
            @RequestParam(defaultValue = "tr") String lang) {
        return ResponseEntity.ok(technologyService.getTechnologyBySlug(slug, lang));
    }

    @PostMapping
    public ResponseEntity<Technology> addOrUpdateTechnology(@RequestBody TechnologyRequest req) {
        return ResponseEntity.ok(technologyService.addOrUpdateTechnology(req.getId(), req.isActive(), req.getImageUrl(), req.getTextContent()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTechnology(@PathVariable Long id) {
        technologyService.deleteTechnology(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/translation")
    public ResponseEntity<TechnologyTranslation> addOrUpdateTranslation(@RequestBody TranslationRequest req) {
        return ResponseEntity.ok(technologyService.addOrUpdateTranslation(req.getTechnologyId(), req.getLangCode(), req.getTitle(), req.getDescription(), req.getFeaturesDescription(), req.getSlug()));
    }

    @PostMapping("/catalog")
    public ResponseEntity<TechnologyCatalog> addOrUpdateCatalog(@RequestBody CatalogRequest req) {
        return ResponseEntity.ok(technologyService.addOrUpdateCatalog(req.getTechnologyId(), req.getName(), req.getFileUrl()));
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(technologyService.uploadFile(file));
    }
    
    // PDF dosyasını serve et
    @GetMapping("/files/{fileName:.+}")
    public ResponseEntity<byte[]> getPdfFile(@PathVariable String fileName) {
        try {
            byte[] fileContent = technologyService.getPdfFile(fileName);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", fileName);
            headers.setContentLength(fileContent.length);
            headers.setCacheControl("no-cache, no-store, must-revalidate");
            headers.setPragma("no-cache");
            headers.setExpires(0);
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileContent);
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    // Sıralama güncelle
    @PutMapping("/reorder")
    public ResponseEntity<Void> reorderTechnologies(@RequestBody ReorderRequest request) {
        technologyService.reorderTechnologies(request.getItems());
        return ResponseEntity.ok().build();
    }

    @Data
    public static class ReorderRequest {
        private List<TechnologyService.ReorderItem> items;
    }

    @Data
    public static class TechnologyRequest {
        private Long id;
        private boolean active;
        private String imageUrl;
        private String textContent;
    }

    @Data
    public static class TranslationRequest {
        private Long technologyId;
        private String langCode;
        private String title;
        private String description;
        private String featuresDescription;
        private String slug;
    }

    @Data
    public static class CatalogRequest {
        private Long technologyId;
        private String name;
        private String fileUrl;
    }
}
