package com.example.nikutek.repository;

import com.example.nikutek.entity.NikuniPompaTechnology;
import com.example.nikutek.entity.NikuniPompaTechnologyTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NikuniPompaTechnologyTranslationRepository extends JpaRepository<NikuniPompaTechnologyTranslation, Long> {
    List<NikuniPompaTechnologyTranslation> findByTechnology(NikuniPompaTechnology technology);
    Optional<NikuniPompaTechnologyTranslation> findBySlug(String slug);
    
    @Query("SELECT t FROM NikuniPompaTechnologyTranslation t WHERE t.slug = :slug AND t.language.code = :langCode")
    Optional<NikuniPompaTechnologyTranslation> findBySlugAndLanguageCode(@Param("slug") String slug, @Param("langCode") String langCode);
}

