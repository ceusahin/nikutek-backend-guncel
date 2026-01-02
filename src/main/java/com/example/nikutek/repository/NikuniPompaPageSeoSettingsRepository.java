package com.example.nikutek.repository;

import com.example.nikutek.entity.NikuniPompaPageSeoSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NikuniPompaPageSeoSettingsRepository extends JpaRepository<NikuniPompaPageSeoSettings, Long> {
    Optional<NikuniPompaPageSeoSettings> findByPageTypeAndLanguage(String pageType, String language);
}

