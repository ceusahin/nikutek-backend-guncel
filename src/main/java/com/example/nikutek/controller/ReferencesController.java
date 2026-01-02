package com.example.nikutek.controller;

import com.example.nikutek.dto.IndustryDTO;
import com.example.nikutek.entity.ReferencesCompany;
import com.example.nikutek.entity.ReferencesIndustry;
import com.example.nikutek.entity.ReferencesIndustryTranslation;
import com.example.nikutek.service.ReferencesService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nikutek/references")
@RequiredArgsConstructor
public class ReferencesController {

    private final ReferencesService referencesService;

    @GetMapping("/{langCode}")
    public ResponseEntity<List<IndustryDTO>> getAll(@PathVariable String langCode) {
        return ResponseEntity.ok(referencesService.getAllByLanguageCode(langCode));
    }

    // 🎛️ Admin paneli için tüm endüstrileri getir (şirket filtresi olmadan)
    @GetMapping("/admin/{langCode}")
    public ResponseEntity<List<IndustryDTO>> getAllForAdmin(@PathVariable String langCode) {
        return ResponseEntity.ok(referencesService.getAllByLanguageCodeForAdmin(langCode));
    }

    @PostMapping("/industry")
    public ResponseEntity<ReferencesIndustry> addOrUpdateIndustry(@RequestParam(required = false) Long id) {
        return ResponseEntity.ok(referencesService.addOrUpdateIndustry(id));
    }

    @DeleteMapping("/industry/{id}")
    public ResponseEntity<Void> deleteIndustry(@PathVariable Long id) {
        referencesService.deleteIndustry(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/industry-translation")
    public ResponseEntity<ReferencesIndustryTranslation> addOrUpdateIndustryTranslation(
            @RequestBody IndustryTranslationRequest request
    ) {
        return ResponseEntity.ok(
                referencesService.addOrUpdateIndustryTranslation(
                        request.getIndustryId(),
                        request.getLanguageCode(),
                        request.getName()
                )
        );
    }

    @PostMapping("/company")
    public ResponseEntity<ReferencesCompany> addOrUpdateCompany(@RequestBody CompanyRequest request) {
        return ResponseEntity.ok(
                referencesService.addOrUpdateCompany(request.getId(), request.getIndustryId(), request.getName())
        );
    }

    @DeleteMapping("/company/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        referencesService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }

    @Data
    public static class CompanyRequest {
        private Long id;
        private Long industryId;
        private String name;
    }

    @Data
    public static class IndustryTranslationRequest {
        private Long industryId;
        private String languageCode;
        private String name;
    }
}
