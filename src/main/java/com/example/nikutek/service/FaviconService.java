package com.example.nikutek.service;

import com.example.nikutek.entity.Favicon;
import com.example.nikutek.repository.FaviconRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class FaviconService {

    private final FaviconRepository faviconRepository;
    private final Cloudinary cloudinary;

    public FaviconService(FaviconRepository faviconRepository, Cloudinary cloudinary) {
        this.faviconRepository = faviconRepository;
        this.cloudinary = cloudinary;
    }

    // Tek favicon döner
    public Favicon getFavicon() {
        return faviconRepository.findAll().stream().findFirst().orElse(null);
    }

    public Favicon uploadFavicon(MultipartFile file) throws IOException {
        if (file.isEmpty()) throw new IllegalArgumentException("Dosya boş olamaz");

        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "folder", "nikutek/favicon",
                        "overwrite", true,
                        "resource_type", "image"
                ));

        String url = (String) uploadResult.get("secure_url");
        String publicId = (String) uploadResult.get("public_id");

        Favicon favicon = getFavicon();
        if (favicon != null) {
            favicon.setImageUrl(url);
            favicon.setPublicId(publicId);
        } else {
            favicon = new Favicon();
            favicon.setTitle("favicon");
            favicon.setImageUrl(url);
            favicon.setPublicId(publicId);
        }

        return faviconRepository.save(favicon);
    }

    public void deleteFavicon() throws IOException {
        Favicon favicon = getFavicon();
        if (favicon != null) {
            if (favicon.getPublicId() != null) {
                cloudinary.uploader().destroy(favicon.getPublicId(), ObjectUtils.emptyMap());
            }
            faviconRepository.delete(favicon);
        }
    }
}

