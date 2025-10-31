package com.example.nikutek.repository;

import com.example.nikutek.entity.Product;
import com.example.nikutek.entity.ProductTranslation;
import com.example.nikutek.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProductTranslationRepository extends JpaRepository<ProductTranslation, Long> {
    List<ProductTranslation> findByProduct(Product product);
    Optional<ProductTranslation> findByProductAndLanguage(Product product, Language language);
    void deleteByProduct(Product product);
    Optional<ProductTranslation> findBySlug(String slug);
    Optional<ProductTranslation> findBySlugAndLanguage(String slug, Language language);
}
