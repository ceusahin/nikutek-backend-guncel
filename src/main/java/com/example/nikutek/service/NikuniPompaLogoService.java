package com.example.nikutek.service;

import com.example.nikutek.entity.NikuniPompaLogo;
import com.example.nikutek.repository.NikuniPompaLogoRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class NikuniPompaLogoService {

    private final NikuniPompaLogoRepository logoRepository;
    private final Cloudinary cloudinary;

    public NikuniPompaLogoService(NikuniPompaLogoRepository logoRepository, Cloudinary cloudinary) {
        this.logoRepository = logoRepository;
        this.cloudinary = cloudinary;
    }

    // Tek logo döner
    public NikuniPompaLogo getLogo() {
        return logoRepository.findAll().stream().findFirst().orElse(null);
    }

    // Logo yükle / güncelle
    public NikuniPompaLogo uploadLogo(MultipartFile file) throws IOException {
        if (file.isEmpty()) throw new IllegalArgumentException("Dosya boş olamaz");

        // Cloudinary'ye yükle
        var uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "folder", "nikunipompa/logo",
                        "overwrite", true,
                        "resource_type", "image"
                ));

        String url = (String) uploadResult.get("secure_url");
        String publicId = (String) uploadResult.get("public_id");

        // Mevcut logoyu al
        NikuniPompaLogo logo = getLogo();
        if (logo != null) {
            logo.setImageUrl(url);
            logo.setPublicId(publicId);
        } else {
            logo = new NikuniPompaLogo();
            logo.setTitle("logo");
            logo.setImageUrl(url);
            logo.setPublicId(publicId);
        }

        return logoRepository.save(logo);
    }

    // Logo sil
    public void deleteLogo() throws IOException {
        NikuniPompaLogo logo = getLogo();
        if (logo != null) {
            if (logo.getPublicId() != null) {
                cloudinary.uploader().destroy(logo.getPublicId(), ObjectUtils.emptyMap());
            }
            logoRepository.delete(logo);
        }
    }
}

