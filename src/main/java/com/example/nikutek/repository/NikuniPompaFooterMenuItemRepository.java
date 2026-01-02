package com.example.nikutek.repository;

import com.example.nikutek.entity.NikuniPompaFooterMenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NikuniPompaFooterMenuItemRepository extends JpaRepository<NikuniPompaFooterMenuItem, Long> {
    List<NikuniPompaFooterMenuItem> findByMenuId(Long menuId);
}

