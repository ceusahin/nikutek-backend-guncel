package com.example.nikutek.repository;

import com.example.nikutek.entity.NikuniPompaProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface NikuniPompaProductRepository extends JpaRepository<NikuniPompaProduct, Long> {
    List<NikuniPompaProduct> findByParentIsNull();
    
    @Query("SELECT p FROM NikuniPompaProduct p WHERE p.parent IS NULL ORDER BY p.displayOrder ASC, p.id ASC")
    List<NikuniPompaProduct> findParentProductsOrdered();
    
    List<NikuniPompaProduct> findByParent(NikuniPompaProduct parent);
}

