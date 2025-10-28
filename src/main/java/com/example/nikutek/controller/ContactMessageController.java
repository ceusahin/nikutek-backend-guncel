package com.example.nikutek.controller;

import com.example.nikutek.dto.ContactMessageDTO;
import com.example.nikutek.entity.ContactMessage;
import com.example.nikutek.service.ContactMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contact")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ContactMessageController {

    private final ContactMessageService contactMessageService;

    // 📨 Kullanıcı form gönderdiğinde
    @PostMapping("/send")
    public ResponseEntity<ContactMessageDTO> sendMessage(@RequestBody ContactMessage message) {
        return ResponseEntity.ok(contactMessageService.saveMessage(message));
    }

    // 📋 Admin paneli için tüm mesajlar
    @GetMapping("/messages")
    public ResponseEntity<List<ContactMessageDTO>> getMessages() {
        return ResponseEntity.ok(contactMessageService.getAllMessages());
    }

    // 🗑️ Mesaj silme (admin panel)
    @DeleteMapping("/messages/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        contactMessageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}
