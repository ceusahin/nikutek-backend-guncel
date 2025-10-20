package com.example.nikutek.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ContactMessageDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String subject;
    private String message;
    private LocalDateTime createdAt;
}