package com.example.nikutek.repository;

import com.example.nikutek.entity.Technology;
import com.example.nikutek.entity.TechnologyTranslation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TechnologyTranslationRepository extends JpaRepository<TechnologyTranslation, Long> {
    List<TechnologyTranslation> findByTechnology(Technology technology);
}