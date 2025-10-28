package com.example.nikutek.controller;

import com.example.nikutek.dto.ChangePasswordDTO;
import com.example.nikutek.repository.AdminUserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminUserController {

    private final AdminUserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public AdminUserController(AdminUserRepository userRepo) {
        this.userRepo = userRepo;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO dto) {
        return userRepo.findByUsernameAndIsActiveTrue(dto.getUsername())
                .map(user -> {
                    String hashed = passwordEncoder.encode(dto.getNewPassword());
                    user.setPasswordHash(hashed);
                    userRepo.save(user);
                    return ResponseEntity.ok("Şifre başarıyla değiştirildi");
                })
                .orElse(ResponseEntity.status(404).body("Kullanıcı bulunamadı"));
    }

    @PostMapping("/reset-password-temporary")
    public ResponseEntity<?> resetPasswordTemporary(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String newPassword = body.get("newPassword");

        return userRepo.findByUsernameAndIsActiveTrue(username)
                .map(user -> {
                    user.setPasswordHash(passwordEncoder.encode(newPassword));
                    userRepo.save(user);
                    return ResponseEntity.ok("Şifre başarıyla sıfırlandı");
                })
                .orElseGet(() -> ResponseEntity.status(404).body("Kullanıcı bulunamadı"));
    }
}