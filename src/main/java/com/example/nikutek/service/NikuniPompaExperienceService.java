package com.example.nikutek.service;

import com.example.nikutek.dto.ExperienceDTO;
import java.util.List;

public interface NikuniPompaExperienceService {
    List<ExperienceDTO> getAll();
    ExperienceDTO saveOrUpdate(ExperienceDTO dto);
    void delete(Long id);
}

