package com.example.nikutek.repository;

import com.example.nikutek.entity.Technology;
import com.example.nikutek.entity.TechnologyTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TechnologyTranslationRepository extends JpaRepository<TechnologyTranslation, Long> {
    List<TechnologyTranslation> findByTechnology(Technology technology);
    Optional<TechnologyTranslation> findBySlug(String slug);
    
    @Query("SELECT t FROM TechnologyTranslation t WHERE t.slug = :slug AND t.language.code = :langCode")
    Optional<TechnologyTranslation> findBySlugAndLanguageCode(@Param("slug") String slug, @Param("langCode") String langCode);
}