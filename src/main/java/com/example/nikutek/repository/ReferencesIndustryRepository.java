package com.example.nikutek.repository;

import com.example.nikutek.entity.ReferencesIndustry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferencesIndustryRepository extends JpaRepository<ReferencesIndustry, Long> {}
