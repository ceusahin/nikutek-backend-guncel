package com.example.nikutek.repository;

import com.example.nikutek.entity.NikuniPompaContactInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NikuniPompaContactInfoRepository extends JpaRepository<NikuniPompaContactInfo, Long> {
}

