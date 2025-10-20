package com.example.nikutek.repository;

import com.example.nikutek.entity.ExperienceTranslation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExperienceTranslationRepository extends JpaRepository<ExperienceTranslation, Long> {
    List<ExperienceTranslation> findByExperienceId(Long experienceId);
}
