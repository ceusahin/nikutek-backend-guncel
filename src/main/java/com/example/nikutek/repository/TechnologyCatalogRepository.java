package com.example.nikutek.repository;

import com.example.nikutek.entity.Technology;
import com.example.nikutek.entity.TechnologyCatalog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TechnologyCatalogRepository extends JpaRepository<TechnologyCatalog, Long> {
    List<TechnologyCatalog> findByTechnology(Technology technology);
}
