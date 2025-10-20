package com.example.nikutek.repository;

import com.example.nikutek.entity.Language;
import com.example.nikutek.entity.ReferencesIndustry;
import com.example.nikutek.entity.ReferencesIndustryTranslation;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ReferencesIndustryTranslationRepository extends CrudRepository<ReferencesIndustryTranslation, Long> {

    ReferencesIndustryTranslation findByIndustryAndLanguage(ReferencesIndustry industry, Language language);

    @Modifying
    @Query("DELETE FROM ReferencesIndustryTranslation t WHERE t.industry.id = :industryId")
    void deleteByIndustryId(@Param("industryId") Long industryId);
}
