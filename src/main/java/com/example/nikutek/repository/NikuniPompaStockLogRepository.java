package com.example.nikutek.repository;

import com.example.nikutek.entity.NikuniPompaStockLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NikuniPompaStockLogRepository extends JpaRepository<NikuniPompaStockLog, Long> {

    List<NikuniPompaStockLog> findByStockIdOrderByActionTimeDesc(Long stockId);

    List<NikuniPompaStockLog> findAllByOrderByActionTimeDesc();
}

