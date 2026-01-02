package com.example.nikutek.repository;

import com.example.nikutek.entity.NikuniPompaProduct;
import com.example.nikutek.entity.NikuniPompaProductFeature;
import com.example.nikutek.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NikuniPompaProductFeatureRepository extends JpaRepository<NikuniPompaProductFeature, Long> {
    List<NikuniPompaProductFeature> findByProduct(NikuniPompaProduct product);
    List<NikuniPompaProductFeature> findByProductAndLanguage(NikuniPompaProduct product, Language language);
    void deleteByProduct(NikuniPompaProduct product);
}

