package com.example.nikutek.repository;

import com.example.nikutek.entity.NikuniPompaMainHero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NikuniPompaMainHeroRepository extends JpaRepository<NikuniPompaMainHero, Long> {
    List<NikuniPompaMainHero> findByLanguageId(Long languageId);
}

