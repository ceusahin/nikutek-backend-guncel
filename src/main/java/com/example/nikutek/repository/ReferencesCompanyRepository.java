package com.example.nikutek.repository;

import com.example.nikutek.entity.ReferencesCompany;
import com.example.nikutek.entity.ReferencesIndustry;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReferencesCompanyRepository extends CrudRepository<ReferencesCompany, Long> {

    List<ReferencesCompany> findByIndustry(ReferencesIndustry industry);

    @Modifying
    @Query("DELETE FROM ReferencesCompany c WHERE c.industry.id = :industryId")
    void deleteByIndustryId(@Param("industryId") Long industryId);
}
