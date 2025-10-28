package com.example.nikutek.controller;

import com.example.nikutek.dto.BlogPostDTO;
import com.example.nikutek.dto.BlogPostImageDTO;
import com.example.nikutek.service.BlogPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/blog")
@RequiredArgsConstructor
public class BlogPostController {

    private final BlogPostService service;

    @GetMapping
    public ResponseEntity<List<BlogPostDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogPostDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BlogPostDTO> create(
            @RequestPart("blogPost") BlogPostDTO dto,
            @RequestPart(value = "images", required = false) MultipartFile[] images
    ) {
        handleImages(dto, images);
        BlogPostDTO saved = service.saveOrUpdate(dto);
        return ResponseEntity.ok(saved);
    }

    @PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BlogPostDTO> update(
            @PathVariable Long id,
            @RequestPart("blogPost") BlogPostDTO dto,
            @RequestPart(value = "images", required = false) MultipartFile[] images
    ) {
        dto.setId(id);
        handleImages(dto, images);
        BlogPostDTO updated = service.saveOrUpdate(dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private void handleImages(BlogPostDTO dto, MultipartFile[] images) {
        if (images != null && images.length > 0) {
            if (dto.getImages() == null) dto.setImages(new ArrayList<>());
            for (MultipartFile file : images) {
                String url = service.uploadImage(file);
                BlogPostImageDTO imgDto = BlogPostImageDTO.builder()
                        .imageUrl(url)
                        .build();
                dto.getImages().add(imgDto);
            }
        }
    }
}
