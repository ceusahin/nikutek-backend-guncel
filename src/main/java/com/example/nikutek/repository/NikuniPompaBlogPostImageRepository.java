package com.example.nikutek.repository;

import com.example.nikutek.entity.NikuniPompaBlogPostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NikuniPompaBlogPostImageRepository extends JpaRepository<NikuniPompaBlogPostImage, Long> {
    List<NikuniPompaBlogPostImage> findByBlogPostIdOrderBySortOrderAsc(Long blogPostId);
}

