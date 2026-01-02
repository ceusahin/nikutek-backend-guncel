package com.example.nikutek.repository;

import com.example.nikutek.entity.NikuniPompaAboutUs;
import com.example.nikutek.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NikuniPompaAboutUsRepository extends JpaRepository<NikuniPompaAboutUs, Long> {
    List<NikuniPompaAboutUs> findByLanguage(Language language);
}

