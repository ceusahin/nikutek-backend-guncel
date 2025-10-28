package com.example.nikutek.controller;

import com.example.nikutek.entity.AdminLog;
import com.example.nikutek.repository.AdminLogRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdminLogController {

    private final AdminLogRepository logRepo;

    public AdminLogController(AdminLogRepository logRepo) {
        this.logRepo = logRepo;
    }

    @GetMapping("/logs")
    public List<AdminLog> getLogs() {
        return logRepo.findAllByOrderByCreatedAtDesc();
    }
}

