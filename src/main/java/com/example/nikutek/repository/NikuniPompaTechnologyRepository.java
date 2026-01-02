package com.example.nikutek.repository;

import com.example.nikutek.entity.NikuniPompaTechnology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NikuniPompaTechnologyRepository extends JpaRepository<NikuniPompaTechnology, Long> {
    @Query("SELECT t FROM NikuniPompaTechnology t ORDER BY t.displayOrder ASC, t.id ASC")
    List<NikuniPompaTechnology> findAllOrdered();
}

