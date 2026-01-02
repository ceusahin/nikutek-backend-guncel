package com.example.nikutek.repository;

import com.example.nikutek.entity.NikuniPompaBlogPost;
import com.example.nikutek.entity.NikuniPompaBlogPostTranslation;
import com.example.nikutek.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NikuniPompaBlogPostTranslationRepository extends JpaRepository<NikuniPompaBlogPostTranslation, Long> {
    Optional<NikuniPompaBlogPostTranslation> findByBlogPostIdAndLanguageCode(Long blogPostId, String languageCode);
    Optional<NikuniPompaBlogPostTranslation> findByBlogPostAndLanguage(NikuniPompaBlogPost blogPost, Language language);

}

