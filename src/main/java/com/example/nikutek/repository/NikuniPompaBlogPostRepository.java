package com.example.nikutek.repository;

import com.example.nikutek.entity.NikuniPompaBlogPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NikuniPompaBlogPostRepository extends JpaRepository<NikuniPompaBlogPost, Long> {
    @Query("SELECT b FROM NikuniPompaBlogPost b ORDER BY b.displayOrder ASC, b.id ASC")
    List<NikuniPompaBlogPost> findAllOrdered();
}

