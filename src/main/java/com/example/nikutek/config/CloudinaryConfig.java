package com.example.nikutek.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "di7lz2aua",
                "api_key", "631396715477245",
                "api_secret", "nW8v_uKZmr0Mc6w8ZKcX0vYuYpo",
                "secure", true
        ));
    }
}