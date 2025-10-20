package com.example.nikutek.service;

import com.example.nikutek.entity.Stock;
import com.example.nikutek.entity.StockLog;
import com.example.nikutek.repository.StockLogRepository;
import com.example.nikutek.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final StockLogRepository stockLogRepository;

    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    @Transactional
    public Stock saveOrUpdateStock(Stock stock) {
        Stock existing = null;
        String actionType = (stock.getId() == null) ? "CREATE" : "UPDATE";

        if (stock.getId() != null) {
            Optional<Stock> opt = stockRepository.findById(stock.getId());
            if (opt.isPresent()) existing = opt.get();
        }

        stock.setLastUpdate(LocalDateTime.now());
        Stock saved = stockRepository.save(stock);

        // Log kaydı oluştur
        StockLog log = new StockLog();
        log.setStockId(saved.getId());
        log.setActionType(actionType);

        if (existing != null) {
            log.setOldAntrepoQuantity(existing.getAntrepoQuantity());
            log.setOldShopQuantity(existing.getShopQuantity());
        }

        log.setNewAntrepoQuantity(saved.getAntrepoQuantity());
        log.setNewShopQuantity(saved.getShopQuantity());
        log.setActionTime(LocalDateTime.now());

        if ("UPDATE".equals(actionType) && existing != null) {
            StringBuilder desc = new StringBuilder();
            if (!existing.getAntrepoQuantity().equals(saved.getAntrepoQuantity())) {
                desc.append(String.format("Antrepo %d → %d. ", existing.getAntrepoQuantity(), saved.getAntrepoQuantity()));
            }
            if (!existing.getShopQuantity().equals(saved.getShopQuantity())) {
                desc.append(String.format("Dükkan %d → %d. ", existing.getShopQuantity(), saved.getShopQuantity()));
            }
            log.setDescription(desc.length() > 0 ? desc.toString().trim() : "Stok bilgisi güncellendi.");
        } else {
            log.setDescription("Yeni stok eklendi.");
        }

        stockLogRepository.save(log);

        return saved;
    }

    @Transactional
    public void deleteStock(Long id) {
        Stock existing = stockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock bulunamadı"));

        // Log oluştur
        StockLog log = new StockLog();
        log.setStockId(existing.getId());
        log.setActionType("DELETE");
        log.setOldAntrepoQuantity(existing.getAntrepoQuantity());
        log.setOldShopQuantity(existing.getShopQuantity());
        log.setNewAntrepoQuantity(null);
        log.setNewShopQuantity(null);
        log.setActionTime(LocalDateTime.now());
        log.setDescription("Stok kaydı silindi.");

        stockLogRepository.save(log);

        // Stock'u sil ve flush
        stockRepository.delete(existing);
        stockRepository.flush();
    }

    public List<StockLog> getLogsByStockId(Long stockId) {
        return stockLogRepository.findByStockIdOrderByActionTimeDesc(stockId);
    }

    public List<StockLog> getAllLogs() {
        return stockLogRepository.findAllByOrderByActionTimeDesc();
    }
}
