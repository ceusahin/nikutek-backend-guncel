package com.example.nikutek.repository;

import com.example.nikutek.entity.NikuniPompaStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NikuniPompaStockRepository extends JpaRepository<NikuniPompaStock, Long> {
}

