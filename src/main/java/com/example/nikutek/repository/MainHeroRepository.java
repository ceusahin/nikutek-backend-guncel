package com.example.nikutek.repository;

import com.example.nikutek.entity.MainHero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MainHeroRepository extends JpaRepository<MainHero, Long> {
    List<MainHero> findByLanguageId(Long languageId);
}
