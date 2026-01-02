package com.example.nikutek.repository;

import com.example.nikutek.entity.NikuniPompaMapSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface NikuniPompaMapSettingsRepository extends JpaRepository<NikuniPompaMapSettings, Long> {
    Optional<NikuniPompaMapSettings> findFirstByIsActiveTrue();
}

