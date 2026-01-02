package com.example.nikutek.repository;

import com.example.nikutek.entity.Language;
import com.example.nikutek.entity.NikuniPompaReferencesIndustry;
import com.example.nikutek.entity.NikuniPompaReferencesIndustryTranslation;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface NikuniPompaReferencesIndustryTranslationRepository extends CrudRepository<NikuniPompaReferencesIndustryTranslation, Long> {

    NikuniPompaReferencesIndustryTranslation findByIndustryAndLanguage(NikuniPompaReferencesIndustry industry, Language language);

    @Modifying
    @Query("DELETE FROM NikuniPompaReferencesIndustryTranslation t WHERE t.industry.id = :industryId")
    void deleteByIndustryId(@Param("industryId") Long industryId);
}

