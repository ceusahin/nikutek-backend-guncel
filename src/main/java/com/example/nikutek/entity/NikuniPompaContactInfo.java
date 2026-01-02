package com.example.nikutek.entity;

import com.example.nikutek.enums.InfoType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "contact_info", schema = "nikunipompa")
public class NikuniPompaContactInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private InfoType type;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @OneToMany(mappedBy = "contactInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NikuniPompaContactInfoTranslation> translations = new ArrayList<>();
}

