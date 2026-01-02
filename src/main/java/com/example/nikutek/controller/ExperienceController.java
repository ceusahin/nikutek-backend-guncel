package com.example.nikutek.controller;

import com.example.nikutek.dto.ExperienceDTO;
import com.example.nikutek.service.ExperienceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nikutek/experience")
public class ExperienceController {

    private final ExperienceService experienceService;

    public ExperienceController(ExperienceService experienceService) {
        this.experienceService = experienceService;
    }

    @GetMapping
    public ResponseEntity<List<ExperienceDTO>> getAll() {
        List<ExperienceDTO> experiences = experienceService.getAll();
        return ResponseEntity.ok(experiences);
    }

    @PostMapping
    public ResponseEntity<ExperienceDTO> saveOrUpdate(@RequestBody ExperienceDTO dto) {
        ExperienceDTO savedDto = experienceService.saveOrUpdate(dto);
        return ResponseEntity.ok(savedDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        experienceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
