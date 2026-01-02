package com.example.nikutek.repository;

import com.example.nikutek.entity.NikuniPompaExperienceTranslation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NikuniPompaExperienceTranslationRepository extends JpaRepository<NikuniPompaExperienceTranslation, Long> {
    List<NikuniPompaExperienceTranslation> findByExperienceId(Long experienceId);
}

