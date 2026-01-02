package com.example.nikutek.repository;

import com.example.nikutek.entity.NikuniPompaSocialMediaSettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NikuniPompaSocialMediaSettingsRepository extends JpaRepository<NikuniPompaSocialMediaSettings, Long> {
    Optional<NikuniPompaSocialMediaSettings> findByPlatform(String platform);
}

