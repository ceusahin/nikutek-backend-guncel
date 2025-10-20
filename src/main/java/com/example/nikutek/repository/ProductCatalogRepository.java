package com.example.nikutek.repository;

import com.example.nikutek.entity.Product;
import com.example.nikutek.entity.ProductCatalog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductCatalogRepository extends JpaRepository<ProductCatalog, Long> {
    List<ProductCatalog> findByProduct(Product product);
    void deleteByProduct(Product product);
}
