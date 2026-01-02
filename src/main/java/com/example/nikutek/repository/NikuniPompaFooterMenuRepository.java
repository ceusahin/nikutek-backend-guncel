package com.example.nikutek.repository;

import com.example.nikutek.entity.NikuniPompaFooterMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NikuniPompaFooterMenuRepository extends JpaRepository<NikuniPompaFooterMenu, Long> {
    List<NikuniPompaFooterMenu> findByLanguage_Id(Long languageId);
}

