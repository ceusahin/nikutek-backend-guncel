package com.example.nikutek.service;

import com.example.nikutek.entity.AdminLog;
import com.example.nikutek.repository.AdminLogRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminLogService {
    private final AdminLogRepository logRepo;

    public AdminLogService(AdminLogRepository logRepo) {
        this.logRepo = logRepo;
    }

    public void log(String username, String action, String entity, Long entityId, String details) {
        AdminLog log = new AdminLog();
        log.setUsername(username);
        log.setAction(action);
        log.setEntity(entity);
        log.setEntityId(entityId);
        log.setDetails(details);
        logRepo.save(log);
    }
}
