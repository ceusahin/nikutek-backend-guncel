package com.example.nikutek.repository;

import com.example.nikutek.entity.StockLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockLogRepository extends JpaRepository<StockLog, Long> {

    List<StockLog> findByStockIdOrderByActionTimeDesc(Long stockId);

    List<StockLog> findAllByOrderByActionTimeDesc();
}