package com.example.nikutek.repository;

import com.example.nikutek.entity.PageSeoSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PageSeoSettingsRepository extends JpaRepository<PageSeoSettings, Long> {
    Optional<PageSeoSettings> findByPageTypeAndLanguage(String pageType, String language);
}

