package com.example.nikutek.controller;

import com.example.nikutek.dto.ProductDTO;
import com.example.nikutek.entity.*;
import com.example.nikutek.service.ProductService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    // 🔸 Slug'a göre ürün çek (SEO-friendly URL)
    @GetMapping("/slug/{slug}")
    public ResponseEntity<ProductDTO> getProductBySlug(
            @PathVariable String slug,
            @RequestParam(defaultValue = "tr") String lang) {
        return ResponseEntity.ok(productService.getProductBySlug(slug, lang));
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

    // 🔸 File upload - PDF'ler local'e, resimler Cloudinary'ye
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(productService.uploadFile(file));
    }
    
    // PDF dosyasını serve et
    @GetMapping("/files/{fileName:.+}")
    public ResponseEntity<byte[]> getPdfFile(@PathVariable String fileName) {
        try {
            byte[] fileContent = productService.getPdfFile(fileName);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            // inline: tarayıcıda aç, attachment: indir
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"");
            headers.setContentLength(fileContent.length);
            headers.setCacheControl("no-cache, no-store, must-revalidate");
            headers.setPragma("no-cache");
            headers.setExpires(0);
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileContent);
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    // 🔸 Sıralama güncelle (sadece parent ürünler için)
    @PutMapping("/reorder")
    public ResponseEntity<Void> reorderProducts(@RequestBody ReorderRequest request) {
        productService.reorderProducts(request.getItems());
        return ResponseEntity.ok().build();
    }

    @Data
    public static class ReorderRequest {
        private List<ProductService.ReorderItem> items;
    }
}

