package com.example.nikutek.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.nikutek.entity.AboutUs;
import com.example.nikutek.service.AboutUsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/about-us")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AboutUsController {

    private final AboutUsService aboutUsService;
    private final Cloudinary cloudinary;

    @GetMapping("/{languageCode}")
    public ResponseEntity<List<AboutUs>> getByLanguage(@PathVariable String languageCode) {
        return ResponseEntity.ok(aboutUsService.getAllByLanguageCode(languageCode));
    }

    @PostMapping
    public ResponseEntity<AboutUs> saveOrUpdate(
            @RequestPart("data") AboutUs aboutUs,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) throws IOException {

        if (file != null && !file.isEmpty()) {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "nikutek/about-us",
                            "resource_type", "image"
                    ));
            String mediaUrl = uploadResult.get("secure_url").toString();
            aboutUs.setMediaUrl(mediaUrl);
        }

        return ResponseEntity.ok(
                aboutUsService.addOrUpdate(aboutUs, aboutUs.getLanguage().getCode())
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        aboutUsService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
