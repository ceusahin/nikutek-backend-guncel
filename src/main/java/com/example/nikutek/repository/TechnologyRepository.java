package com.example.nikutek.repository;

import com.example.nikutek.entity.Technology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TechnologyRepository extends JpaRepository<Technology, Long> {
    @Query("SELECT t FROM Technology t ORDER BY t.displayOrder ASC, t.id ASC")
    List<Technology> findAllOrdered();
}
