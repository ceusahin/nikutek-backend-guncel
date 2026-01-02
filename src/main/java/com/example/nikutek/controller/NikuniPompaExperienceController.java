package com.example.nikutek.controller;

import com.example.nikutek.dto.ExperienceDTO;
import com.example.nikutek.service.NikuniPompaExperienceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nikuni-pompa/experience")
public class NikuniPompaExperienceController {

    private final NikuniPompaExperienceService experienceService;

    public NikuniPompaExperienceController(NikuniPompaExperienceService experienceService) {
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

