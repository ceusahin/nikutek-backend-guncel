package com.example.nikutek.repository;

import com.example.nikutek.entity.NikuniPompaTechnology;
import com.example.nikutek.entity.NikuniPompaTechnologyCatalog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NikuniPompaTechnologyCatalogRepository extends JpaRepository<NikuniPompaTechnologyCatalog, Long> {
    List<NikuniPompaTechnologyCatalog> findByTechnology(NikuniPompaTechnology technology);
}

