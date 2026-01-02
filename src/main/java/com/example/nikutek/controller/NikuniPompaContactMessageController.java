package com.example.nikutek.controller;

import com.example.nikutek.dto.ContactMessageDTO;
import com.example.nikutek.entity.NikuniPompaContactMessage;
import com.example.nikutek.service.NikuniPompaContactMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nikuni-pompa/contact")
@RequiredArgsConstructor
public class NikuniPompaContactMessageController {

    private final NikuniPompaContactMessageService contactMessageService;

    // 📨 Kullanıcı form gönderdiğinde
    @PostMapping("/send")
    public ResponseEntity<ContactMessageDTO> sendMessage(@RequestBody NikuniPompaContactMessage message) {
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

