package com.example.nikutek.repository;

import com.example.nikutek.entity.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    @Query("SELECT b FROM BlogPost b ORDER BY b.displayOrder ASC, b.id ASC")
    List<BlogPost> findAllOrdered();
}
