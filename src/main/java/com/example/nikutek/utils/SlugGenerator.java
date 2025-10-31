package com.example.nikutek.utils;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SlugGenerator {

    private static final Map<String, String> TURKISH_CHAR_MAP = new HashMap<>();
    
    static {
        TURKISH_CHAR_MAP.put("ş", "s");
        TURKISH_CHAR_MAP.put("Ş", "s");
        TURKISH_CHAR_MAP.put("ğ", "g");
        TURKISH_CHAR_MAP.put("Ğ", "g");
        TURKISH_CHAR_MAP.put("ü", "u");
        TURKISH_CHAR_MAP.put("Ü", "u");
        TURKISH_CHAR_MAP.put("ö", "o");
        TURKISH_CHAR_MAP.put("Ö", "o");
        TURKISH_CHAR_MAP.put("ı", "i");
        TURKISH_CHAR_MAP.put("İ", "i");
        TURKISH_CHAR_MAP.put("ç", "c");
        TURKISH_CHAR_MAP.put("Ç", "c");
    }

    /**
     * Title'dan slug oluşturur
     * @param title Başlık metni
     * @return Slug (örn: "Pompa Sistemi" -> "pompa-sistemi")
     */
    public static String generateSlug(String title) {
        if (title == null || title.trim().isEmpty()) {
            return "";
        }

        // Türkçe karakterleri dönüştür
        String slug = title;
        for (Map.Entry<String, String> entry : TURKISH_CHAR_MAP.entrySet()) {
            slug = slug.replace(entry.getKey(), entry.getValue());
        }

        // Küçük harfe çevir
        slug = slug.toLowerCase();

        // Özel karakterleri ve boşlukları tire ile değiştir
        slug = slug.replaceAll("[^a-z0-9]+", "-");

        // Başındaki ve sonundaki tire'leri temizle
        slug = slug.replaceAll("^-+|-+$", "");

        // Maksimum 255 karakter (database constraint)
        if (slug.length() > 255) {
            slug = slug.substring(0, 255);
            slug = slug.replaceAll("-+$", ""); // Son tire'yi temizle
        }

        return slug;
    }

    /**
     * Slug'un unique olmasını sağlar
     * Eğer slug mevcutsa -2, -3 gibi suffix ekler
     * @param baseSlug Temel slug
     * @param checkExists Slug'un mevcut olup olmadığını kontrol eden fonksiyon
     * @param excludeId Slug kontrolünden hariç tutulacak ID (güncelleme için)
     * @return Unique slug
     */
    public static String ensureUniqueSlug(String baseSlug, 
                                         java.util.function.Function<String, Boolean> checkExists,
                                         Long excludeId) {
        if (baseSlug == null || baseSlug.isEmpty()) {
            return baseSlug;
        }

        String slug = baseSlug;
        int counter = 2;

        // Eğer slug zaten varsa ve excludeId değilse, -2, -3 gibi ekle
        while (checkExists.apply(slug)) {
            slug = baseSlug + "-" + counter;
            counter++;
            
            // Sonsuz döngüden kaçınmak için
            if (counter > 1000) {
                slug = baseSlug + "-" + System.currentTimeMillis();
                break;
            }
        }

        return slug;
    }
}

