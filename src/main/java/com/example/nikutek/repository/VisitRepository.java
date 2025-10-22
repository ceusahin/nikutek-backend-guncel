package com.example.nikutek.repository;

import com.example.nikutek.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface VisitRepository extends JpaRepository<Visit, Long> {

    @Query("SELECT COUNT(v) FROM Visit v WHERE v.timestamp >= :start")
    long countVisitsSince(LocalDateTime start);
}