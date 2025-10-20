package com.example.nikutek.controller;

import com.example.nikutek.dto.UserLoginDTO;
import com.example.nikutek.repository.AdminUserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AdminUserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AdminUserRepository userRepo) {
        this.userRepo = userRepo;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO loginDTO) {
        logger.info("Login attempt: username={}", loginDTO.getUsername());

        if (loginDTO.getUsername() == null || loginDTO.getPassword() == null) {
            logger.warn("Login failed: username veya password boş");
            return ResponseEntity.badRequest().body("Username veya password boş");
        }

        return userRepo.findByUsernameAndIsActiveTrue(loginDTO.getUsername())
                .map(user -> {
                    boolean matches = passwordEncoder.matches(loginDTO.getPassword(), user.getPasswordHash());
                    logger.info("Password match result: {}", matches);

                    if (matches) {
                        logger.info("Login successful for user {}", user.getUsername());
                        return ResponseEntity.ok("admin-token");
                    } else {
                        logger.warn("Login failed: şifre yanlış");
                        return ResponseEntity.status(401).body("Kullanıcı adı veya şifre yanlış");
                    }
                })
                .orElseGet(() -> {
                    logger.warn("Login failed: kullanıcı bulunamadı");
                    return ResponseEntity.status(401).body("Kullanıcı bulunamadı");
                });
    }
}

