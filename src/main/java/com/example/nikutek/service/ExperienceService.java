package com.example.nikutek.service;

import com.example.nikutek.dto.ExperienceDTO;
import java.util.List;

public interface ExperienceService {
    List<ExperienceDTO> getAll();
    ExperienceDTO saveOrUpdate(ExperienceDTO dto);
    void delete(Long id);
}
