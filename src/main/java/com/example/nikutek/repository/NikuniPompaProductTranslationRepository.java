package com.example.nikutek.repository;

import com.example.nikutek.entity.NikuniPompaProduct;
import com.example.nikutek.entity.NikuniPompaProductTranslation;
import com.example.nikutek.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface NikuniPompaProductTranslationRepository extends JpaRepository<NikuniPompaProductTranslation, Long> {
    List<NikuniPompaProductTranslation> findByProduct(NikuniPompaProduct product);
    Optional<NikuniPompaProductTranslation> findByProductAndLanguage(NikuniPompaProduct product, Language language);
    void deleteByProduct(NikuniPompaProduct product);
    Optional<NikuniPompaProductTranslation> findBySlug(String slug);
    Optional<NikuniPompaProductTranslation> findBySlugAndLanguage(String slug, Language language);
}

