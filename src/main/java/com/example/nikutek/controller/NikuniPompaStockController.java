package com.example.nikutek.controller;

import com.example.nikutek.entity.NikuniPompaStock;
import com.example.nikutek.entity.NikuniPompaStockLog;
import com.example.nikutek.service.NikuniPompaStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nikuni-pompa/stocks")
@RequiredArgsConstructor
public class NikuniPompaStockController {

    private final NikuniPompaStockService stockService;

    @GetMapping
    public List<NikuniPompaStock> getAllStocks() {
        return stockService.getAllStocks();
    }

    @PostMapping
    public NikuniPompaStock saveStock(@RequestBody NikuniPompaStock stock) {
        return stockService.saveOrUpdateStock(stock);
    }

    @PutMapping("/{id}")
    public NikuniPompaStock updateStock(@PathVariable Long id, @RequestBody NikuniPompaStock stock) {
        stock.setId(id);
        return stockService.saveOrUpdateStock(stock);
    }

    @DeleteMapping("/{id}")
    public void deleteStock(@PathVariable Long id) {
        stockService.deleteStock(id);
    }

    @GetMapping("/{id}/logs")
    public List<NikuniPompaStockLog> getLogsByStockId(@PathVariable Long id) {
        return stockService.getLogsByStockId(id);
    }

    @GetMapping("/logs")
    public List<NikuniPompaStockLog> getAllLogs() {
        return stockService.getAllLogs();
    }
}

