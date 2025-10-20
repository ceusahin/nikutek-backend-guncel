package com.example.nikutek.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.nikutek.dto.*;
import com.example.nikutek.entity.*;
import com.example.nikutek.enums.BlogPostType;
import com.example.nikutek.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BlogPostService {

    private final BlogPostRepository blogPostRepository;
    private final BlogPostTranslationRepository translationRepository;
    private final BlogPostImageRepository imageRepository;
    private final Cloudinary cloudinary;
    private final LanguageRepository languageRepository;

    // --- List ---
    public List<BlogPostDTO> getAll() {
        return blogPostRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // --- Get by ID ---
    public BlogPostDTO getById(Long id) {
        return blogPostRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("BlogPost not found"));
    }

    // --- Save/Update ---
    public BlogPostDTO saveOrUpdate(BlogPostDTO dto) {
        BlogPost blogPost = dto.getId() != null
                ? blogPostRepository.findById(dto.getId()).orElse(new BlogPost())
                : new BlogPost();

        blogPost.setType(dto.getType());
        blogPost.setActive(dto.getActive() != null ? dto.getActive() : true);

        // Video post ise videoId çıkar
        if(blogPost.getType() == BlogPostType.video && dto.getVideoLink() != null) {
            blogPost.setVideoId(extractYoutubeVideoId(dto.getVideoLink()));
        }

        BlogPost saved = blogPostRepository.save(blogPost);

        // --- Translations ---
        if(dto.getTranslations() != null) {
            for(BlogPostTranslationDTO tDto : dto.getTranslations()) {

                // --- LANGUAGE CODE TO LANGUAGE ENTITY ---
                Language lang = languageRepository.findByCode(tDto.getLanguageCode())
                        .orElseThrow(() -> new RuntimeException("Dil bulunamadı: " + tDto.getLanguageCode()));

                // Eğer ID varsa direkt bul, yoksa aynı blogPost ve language_id var mı kontrol et
                BlogPostTranslation translation;
                if(tDto.getId() != null) {
                    translation = translationRepository.findById(tDto.getId())
                            .orElse(new BlogPostTranslation());
                } else {
                    translation = translationRepository.findByBlogPostAndLanguage(saved, lang)
                            .orElse(new BlogPostTranslation());
                }

                translation.setBlogPost(saved);
                translation.setLanguage(lang);
                translation.setTitle(tDto.getTitle());
                translation.setDescription(tDto.getDescription());

                translationRepository.save(translation);
            }
        }

        // --- Images ---
        if(dto.getImages() != null) {
            for(BlogPostImageDTO iDto : dto.getImages()) {
                BlogPostImage image = iDto.getId() != null
                        ? imageRepository.findById(iDto.getId()).orElse(new BlogPostImage())
                        : new BlogPostImage();

                image.setBlogPost(saved);
                image.setImageUrl(iDto.getImageUrl());
                image.setAltText(iDto.getAltText());
                image.setSortOrder(iDto.getSortOrder());

                imageRepository.save(image);
            }
        }

        return toDTO(saved);
    }


    // --- Delete ---
    public void delete(Long id) {
        blogPostRepository.deleteById(id);
    }

    // --- Cloudinary Upload ---
    public String uploadImage(MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "nikutek/blog",
                            "overwrite", true,
                            "resource_type", "image"
                    ));
            return uploadResult.get("secure_url").toString();
        } catch (Exception e) {
            throw new RuntimeException("Dosya yüklenemedi: " + e.getMessage());
        }
    }

    // --- Helper: YouTube video ID extract ---
    private String extractYoutubeVideoId(String videoLink) {
        if(videoLink == null) return null;

        if(videoLink.matches("^[a-zA-Z0-9_-]{11}$")) {
            return videoLink;
        }

        if(videoLink.contains("v=")) return videoLink.substring(videoLink.indexOf("v=") + 2).split("&")[0];
        if(videoLink.contains("youtu.be/")) return videoLink.substring(videoLink.lastIndexOf("/") + 1).split("\\?")[0];

        return null;
    }

    // --- Mapper ---
    private BlogPostDTO toDTO(BlogPost blogPost) {
        List<BlogPostTranslationDTO> translations = blogPost.getTranslations() != null
                ? blogPost.getTranslations().stream()
                .map(t -> BlogPostTranslationDTO.builder()
                        .id(t.getId())
                        .languageCode(t.getLanguage().getCode())
                        .title(t.getTitle())
                        .description(t.getDescription())
                        .build())
                .collect(Collectors.toList())
                : null;

        List<BlogPostImageDTO> images = blogPost.getImages() != null
                ? blogPost.getImages().stream()
                .map(i -> BlogPostImageDTO.builder()
                        .id(i.getId())
                        .imageUrl(i.getImageUrl())
                        .altText(i.getAltText())
                        .sortOrder(i.getSortOrder())
                        .build())
                .collect(Collectors.toList())
                : null;

        String videoLink = blogPost.getVideoId() != null ? "https://www.youtube.com/watch?v=" + blogPost.getVideoId() : null;

        return BlogPostDTO.builder()
                .id(blogPost.getId())
                .type(blogPost.getType())
                .videoLink(videoLink)
                .active(blogPost.getActive())
                .translations(translations)
                .images(images)
                .build();
    }
}
