package com.example.nikutek.repository;

import com.example.nikutek.entity.NikuniPompaProduct;
import com.example.nikutek.entity.NikuniPompaProductCatalog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NikuniPompaProductCatalogRepository extends JpaRepository<NikuniPompaProductCatalog, Long> {
    List<NikuniPompaProductCatalog> findByProduct(NikuniPompaProduct product);
    void deleteByProduct(NikuniPompaProduct product);
}

