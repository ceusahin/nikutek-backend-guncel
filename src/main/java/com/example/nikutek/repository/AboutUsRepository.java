package com.example.nikutek.repository;

import com.example.nikutek.entity.AboutUs;
import com.example.nikutek.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AboutUsRepository extends JpaRepository<AboutUs, Long> {
    List<AboutUs> findByLanguage(Language language);
}
