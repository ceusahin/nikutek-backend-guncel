package com.example.nikutek.service;

import com.example.nikutek.entity.NikuniPompaFavicon;
import com.example.nikutek.repository.NikuniPompaFaviconRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class NikuniPompaFaviconService {

    private final NikuniPompaFaviconRepository faviconRepository;
    private final Cloudinary cloudinary;

    public NikuniPompaFaviconService(NikuniPompaFaviconRepository faviconRepository, Cloudinary cloudinary) {
        this.faviconRepository = faviconRepository;
        this.cloudinary = cloudinary;
    }

    // Tek favicon döner
    public NikuniPompaFavicon getFavicon() {
        return faviconRepository.findAll().stream().findFirst().orElse(null);
    }

    public NikuniPompaFavicon uploadFavicon(MultipartFile file) throws IOException {
        if (file.isEmpty()) throw new IllegalArgumentException("Dosya boş olamaz");

        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "folder", "nikunipompa/favicon",
                        "overwrite", true,
                        "resource_type", "image"
                ));

        String url = (String) uploadResult.get("secure_url");
        String publicId = (String) uploadResult.get("public_id");

        NikuniPompaFavicon favicon = getFavicon();
        if (favicon != null) {
            favicon.setImageUrl(url);
            favicon.setPublicId(publicId);
        } else {
            favicon = new NikuniPompaFavicon();
            favicon.setTitle("favicon");
            favicon.setImageUrl(url);
            favicon.setPublicId(publicId);
        }

        return faviconRepository.save(favicon);
    }

    public void deleteFavicon() throws IOException {
        NikuniPompaFavicon favicon = getFavicon();
        if (favicon != null) {
            if (favicon.getPublicId() != null) {
                cloudinary.uploader().destroy(favicon.getPublicId(), ObjectUtils.emptyMap());
            }
            faviconRepository.delete(favicon);
        }
    }
}

