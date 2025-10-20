package com.example.nikutek.repository;

import com.example.nikutek.entity.Product;
import com.example.nikutek.entity.ProductFeature;
import com.example.nikutek.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductFeatureRepository extends JpaRepository<ProductFeature, Long> {
    List<ProductFeature> findByProduct(Product product);
    List<ProductFeature> findByProductAndLanguage(Product product, Language language);
    void deleteByProduct(Product product);
}
