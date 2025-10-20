package com.example.nikutek.service;

import com.example.nikutek.dto.ContactMessageDTO;
import com.example.nikutek.entity.ContactMessage;
import com.example.nikutek.repository.ContactMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContactMessageService {

    private final ContactMessageRepository contactMessageRepository;

    // 📨 Mesaj kaydet (frontend form gönderimi)
    public ContactMessageDTO saveMessage(ContactMessage message) {
        ContactMessage saved = contactMessageRepository.save(message);
        return toDTO(saved);
    }

    // 📋 Tüm mesajları listele (createdAt'e göre ters sırayla)
    public List<ContactMessageDTO> getAllMessages() {
        return contactMessageRepository.findAll()
                .stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // 🗑️ Mesaj sil
    public void deleteMessage(Long id) {
        contactMessageRepository.deleteById(id);
    }

    // 🧠 Entity -> DTO dönüşümü
    private ContactMessageDTO toDTO(ContactMessage entity) {
        ContactMessageDTO dto = new ContactMessageDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setPhone(entity.getPhone());
        dto.setSubject(entity.getSubject());
        dto.setMessage(entity.getMessage());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }
}
