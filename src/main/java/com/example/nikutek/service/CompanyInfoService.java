package com.example.nikutek.service;

import com.example.nikutek.dto.CompanyInfoDTO;
import com.example.nikutek.entity.CompanyInfo;
import com.example.nikutek.entity.Language;
import com.example.nikutek.repository.CompanyInfoRepository;
import com.example.nikutek.repository.LanguageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyInfoService {

    private final CompanyInfoRepository companyInfoRepository;
    private final LanguageRepository languageRepository;

    public CompanyInfoDTO getByLanguageCode(String languageCode) {
        Language language = languageRepository.findByCode(languageCode)
                .orElseThrow(() -> new RuntimeException("Dil bulunamadı: " + languageCode));

        CompanyInfo companyInfo = companyInfoRepository.findByLanguageId(language.getId())
                .orElse(null);

        if (companyInfo == null) return null;

        return new CompanyInfoDTO(
                companyInfo.getId(),
                companyInfo.getCompanyName(),
                companyInfo.getCompanyDescription(),
                language.getCode()
        );
    }

    public CompanyInfoDTO saveOrUpdate(CompanyInfoDTO dto) {
        Language language = languageRepository.findByCode(dto.getLanguageCode())
                .orElseThrow(() -> new RuntimeException("Dil bulunamadı: " + dto.getLanguageCode()));

        CompanyInfo entity = companyInfoRepository.findByLanguageId(language.getId())
                .orElse(new CompanyInfo());

        entity.setCompanyName(dto.getCompanyName());
        entity.setCompanyDescription(dto.getCompanyDescription());
        entity.setLanguage(language);

        companyInfoRepository.save(entity);

        return new CompanyInfoDTO(
                entity.getId(),
                entity.getCompanyName(),
                entity.getCompanyDescription(),
                language.getCode()
        );
    }

    public void delete(Long id) {
        companyInfoRepository.deleteById(id);
    }
}
