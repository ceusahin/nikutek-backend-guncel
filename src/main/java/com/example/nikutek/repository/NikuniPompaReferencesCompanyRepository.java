package com.example.nikutek.repository;

import com.example.nikutek.entity.NikuniPompaReferencesCompany;
import com.example.nikutek.entity.NikuniPompaReferencesIndustry;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NikuniPompaReferencesCompanyRepository extends CrudRepository<NikuniPompaReferencesCompany, Long> {

    List<NikuniPompaReferencesCompany> findByIndustry(NikuniPompaReferencesIndustry industry);

    @Modifying
    @Query("DELETE FROM NikuniPompaReferencesCompany c WHERE c.industry.id = :industryId")
    void deleteByIndustryId(@Param("industryId") Long industryId);
}

