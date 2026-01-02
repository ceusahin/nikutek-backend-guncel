package com.example.nikutek.controller;

import com.example.nikutek.entity.Stock;
import com.example.nikutek.entity.StockLog;
import com.example.nikutek.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nikutek/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @GetMapping
    public List<Stock> getAllStocks() {
        return stockService.getAllStocks();
    }

    @PostMapping
    public Stock saveStock(@RequestBody Stock stock) {
        return stockService.saveOrUpdateStock(stock);
    }

    @PutMapping("/{id}")
    public Stock updateStock(@PathVariable Long id, @RequestBody Stock stock) {
        stock.setId(id);
        return stockService.saveOrUpdateStock(stock);
    }

    @DeleteMapping("/{id}")
    public void deleteStock(@PathVariable Long id) {
        stockService.deleteStock(id);
    }

    @GetMapping("/{id}/logs")
    public List<StockLog> getLogsByStockId(@PathVariable Long id) {
        return stockService.getLogsByStockId(id);
    }

    @GetMapping("/logs")
    public List<StockLog> getAllLogs() {
        return stockService.getAllLogs();
    }
}