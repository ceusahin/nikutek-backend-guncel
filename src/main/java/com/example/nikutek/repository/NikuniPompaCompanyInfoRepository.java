package com.example.nikutek.repository;

import com.example.nikutek.entity.NikuniPompaCompanyInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NikuniPompaCompanyInfoRepository extends JpaRepository<NikuniPompaCompanyInfo, Long> {
    Optional<NikuniPompaCompanyInfo> findByLanguageId(Long languageId);
}

