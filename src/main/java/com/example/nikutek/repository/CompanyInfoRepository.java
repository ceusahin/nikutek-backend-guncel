package com.example.nikutek.repository;

import com.example.nikutek.entity.CompanyInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyInfoRepository extends JpaRepository<CompanyInfo, Long> {
    Optional<CompanyInfo> findByLanguageId(Long languageId);
}
