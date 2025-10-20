package com.example.nikutek.service;

import com.example.nikutek.entity.Logo;
import com.example.nikutek.repository.LogoRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class LogoService {

    private final LogoRepository logoRepository;
    private final Cloudinary cloudinary;

    public LogoService(LogoRepository logoRepository, Cloudinary cloudinary) {
        this.logoRepository = logoRepository;
        this.cloudinary = cloudinary;
    }

    // Tek logo döner
    public Logo getLogo() {
        return logoRepository.findAll().stream().findFirst().orElse(null);
    }

    // Logo yükle / güncelle
    public Logo uploadLogo(MultipartFile file) throws IOException {
        if (file.isEmpty()) throw new IllegalArgumentException("Dosya boş olamaz");

        // Cloudinary'ye yükle
        var uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "folder", "nikutek/logo",
                        "overwrite", true,
                        "resource_type", "image"
                ));

        String url = (String) uploadResult.get("secure_url");
        String publicId = (String) uploadResult.get("public_id");

        // Mevcut logoyu al
        Logo logo = getLogo();
        if (logo != null) {
            logo.setImageUrl(url);
            logo.setPublicId(publicId);
        } else {
            logo = new Logo();
            logo.setTitle("logo");
            logo.setImageUrl(url);
            logo.setPublicId(publicId);
        }

        return logoRepository.save(logo);
    }

    // Logo sil
    public void deleteLogo() throws IOException {
        Logo logo = getLogo();
        if (logo != null) {
            if (logo.getPublicId() != null) {
                cloudinary.uploader().destroy(logo.getPublicId(), ObjectUtils.emptyMap());
            }
            logoRepository.delete(logo);
        }
    }
}
