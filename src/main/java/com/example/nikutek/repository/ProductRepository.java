package com.example.nikutek.repository;

import com.example.nikutek.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByParentIsNull();
    List<Product> findByParent(Product parent);
}
