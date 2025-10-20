package com.example.nikutek.repository;

import com.example.nikutek.entity.SocialMediaSettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SocialMediaSettingsRepository extends JpaRepository<SocialMediaSettings, Long> {
    Optional<SocialMediaSettings> findByPlatform(String platform);
}
