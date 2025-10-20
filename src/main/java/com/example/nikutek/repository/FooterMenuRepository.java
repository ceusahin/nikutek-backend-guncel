package com.example.nikutek.repository;

import com.example.nikutek.entity.FooterMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FooterMenuRepository extends JpaRepository<FooterMenu, Long> {
    List<FooterMenu> findByLanguage_Id(Long languageId);
}
