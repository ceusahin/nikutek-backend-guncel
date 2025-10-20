package com.example.nikutek.repository;

import com.example.nikutek.entity.FooterMenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FooterMenuItemRepository extends JpaRepository<FooterMenuItem, Long> {
    List<FooterMenuItem> findByMenuId(Long menuId);
}
