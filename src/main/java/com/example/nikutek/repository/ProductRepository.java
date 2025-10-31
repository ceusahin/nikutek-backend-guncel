package com.example.nikutek.repository;

import com.example.nikutek.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByParentIsNull();
    
    @Query("SELECT p FROM Product p WHERE p.parent IS NULL ORDER BY p.displayOrder ASC, p.id ASC")
    List<Product> findParentProductsOrdered();
    
    List<Product> findByParent(Product parent);
}
