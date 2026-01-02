package com.example.nikutek.controller;

import com.example.nikutek.entity.Visit;
import com.example.nikutek.repository.VisitRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/nikutek/stats")
public class StatsController {

    private final VisitRepository visitRepository;

    public StatsController(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
    }

    @PostMapping("/add")
    public Map<String, Object> addVisit() {
        Visit visit = new Visit();
        visitRepository.save(visit);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        return response;
    }

    @GetMapping
    public Map<String, Long> getStats() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneDayAgo = now.minusDays(1);
        LocalDateTime oneWeekAgo = now.minusWeeks(1);
        LocalDateTime oneMonthAgo = now.minusMonths(1);

        long daily = visitRepository.countVisitsSince(oneDayAgo);
        long weekly = visitRepository.countVisitsSince(oneWeekAgo);
        long monthly = visitRepository.countVisitsSince(oneMonthAgo);
        long total = visitRepository.count();

        Map<String, Long> stats = new HashMap<>();
        stats.put("daily", daily);
        stats.put("weekly", weekly);
        stats.put("monthly", monthly);
        stats.put("total", total);

        return stats;
    }
}