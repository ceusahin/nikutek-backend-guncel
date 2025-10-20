package com.example.nikutek.repository;

import com.example.nikutek.entity.BlogPostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogPostImageRepository extends JpaRepository<BlogPostImage, Long> {
    List<BlogPostImage> findByBlogPostIdOrderBySortOrderAsc(Long blogPostId);
}
