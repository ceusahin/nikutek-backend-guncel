package com.example.nikutek.service;

import com.example.nikutek.entity.NikuniPompaStock;
import com.example.nikutek.entity.NikuniPompaStockLog;
import com.example.nikutek.repository.NikuniPompaStockLogRepository;
import com.example.nikutek.repository.NikuniPompaStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NikuniPompaStockService {

    private final NikuniPompaStockRepository stockRepository;
    private final NikuniPompaStockLogRepository stockLogRepository;

    public List<NikuniPompaStock> getAllStocks() {
        return stockRepository.findAll();
    }

    @Transactional
    public NikuniPompaStock saveOrUpdateStock(NikuniPompaStock stock) {
        NikuniPompaStock existing = null;
        String actionType = (stock.getId() == null) ? "CREATE" : "UPDATE";

        if (stock.getId() != null) {
            Optional<NikuniPompaStock> opt = stockRepository.findById(stock.getId());
            if (opt.isPresent()) existing = opt.get();
        }

        stock.setLastUpdate(LocalDateTime.now());
        NikuniPompaStock saved = stockRepository.save(stock);

        // Log kaydı oluştur
        NikuniPompaStockLog log = new NikuniPompaStockLog();
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
        NikuniPompaStock existing = stockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock bulunamadı"));

        // Log oluştur
        NikuniPompaStockLog log = new NikuniPompaStockLog();
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

    public List<NikuniPompaStockLog> getLogsByStockId(Long stockId) {
        return stockLogRepository.findByStockIdOrderByActionTimeDesc(stockId);
    }

    public List<NikuniPompaStockLog> getAllLogs() {
        return stockLogRepository.findAllByOrderByActionTimeDesc();
    }
}

