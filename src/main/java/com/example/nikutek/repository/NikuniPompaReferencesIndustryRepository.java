package com.example.nikutek.repository;

import com.example.nikutek.entity.NikuniPompaReferencesIndustry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NikuniPompaReferencesIndustryRepository extends JpaRepository<NikuniPompaReferencesIndustry, Long> {}

