package com.example.nikutek.repository;

import com.example.nikutek.entity.BlogPost;
import com.example.nikutek.entity.BlogPostTranslation;
import com.example.nikutek.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlogPostTranslationRepository extends JpaRepository<BlogPostTranslation, Long> {
    Optional<BlogPostTranslation> findByBlogPostIdAndLanguageCode(Long blogPostId, String languageCode);
    Optional<BlogPostTranslation> findByBlogPostAndLanguage(BlogPost blogPost, Language language);

}