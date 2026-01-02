package com.example.nikutek.repository;

import com.example.nikutek.entity.NikuniPompaVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface NikuniPompaVisitRepository extends JpaRepository<NikuniPompaVisit, Long> {

    @Query("SELECT COUNT(v) FROM NikuniPompaVisit v WHERE v.timestamp >= :start")
    long countVisitsSince(LocalDateTime start);
}

