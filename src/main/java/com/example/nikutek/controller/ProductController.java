package com.example.nikutek.controller;

import com.example.nikutek.dto.ProductDTO;
import com.example.nikutek.entity.*;
import com.example.nikutek.service.ProductService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 🔸 Tüm ürünleri (ana + altlarıyla birlikte) çek
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // 🔸 Belirli ürünün detayını (alt verilerle birlikte) çek
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    // 🔸 Ürün kaydet veya güncelle (tek istekle)
    @PostMapping("/save")
    public ResponseEntity<ProductDTO> saveOrUpdateProduct(@RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.saveOrUpdateFullProduct(productDTO));
    }

    // 🔸 Ürün sil (child ve bağlı veriler dahil)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // 🔸 Aktif/Pasif değiştir
    @PostMapping("/{id}/toggle")
    public ResponseEntity<ProductDTO> toggleActive(@PathVariable Long id) {
        return ResponseEntity.ok(productService.toggleActiveDTO(id));
    }

    // 🔸 Cloudinary dosya yükleme
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(productService.uploadFile(file));
    }
}

