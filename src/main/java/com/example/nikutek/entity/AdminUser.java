package com.example.nikutek.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "admin_user", schema = "nikutek")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String passwordHash;
    private boolean isActive = true;
}