package com.example.nikutek.repository;

import com.example.nikutek.entity.MapSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MapSettingsRepository extends JpaRepository<MapSettings, Long> {
    Optional<MapSettings> findFirstByIsActiveTrue();
}
