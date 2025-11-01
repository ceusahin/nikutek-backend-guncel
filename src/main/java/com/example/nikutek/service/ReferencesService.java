package com.example.nikutek.service;

import com.example.nikutek.dto.CompanyDTO;
import com.example.nikutek.dto.IndustryDTO;
import com.example.nikutek.entity.Language;
import com.example.nikutek.entity.ReferencesCompany;
import com.example.nikutek.entity.ReferencesIndustry;
import com.example.nikutek.entity.ReferencesIndustryTranslation;
import com.example.nikutek.repository.LanguageRepository;
import com.example.nikutek.repository.ReferencesCompanyRepository;
import com.example.nikutek.repository.ReferencesIndustryRepository;
import com.example.nikutek.repository.ReferencesIndustryTranslationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReferencesService {

    private final ReferencesIndustryRepository industryRepository;
    private final ReferencesIndustryTranslationRepository translationRepository;
    private final ReferencesCompanyRepository companyRepository;
    private final LanguageRepository languageRepository;

    // 🌍 Tüm endüstrileri, seçilen dile göre getir
    // Sadece seçilen dilde çevirisi olan endüstrileri döndür (şirket filtresi yok)
    public List<IndustryDTO> getAllByLanguageCode(String code) {
        Language language = languageRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Dil bulunamadı: " + code));

        return industryRepository.findAll().stream()
                .filter(industry -> {
                    // Sadece çevirisi var mı kontrol et (şirket kontrolü yok)
                    ReferencesIndustryTranslation translation =
                            translationRepository.findByIndustryAndLanguage(industry, language);
                    return translation != null && translation.getName() != null && !translation.getName().trim().isEmpty();
                })
                .map(industry -> {
                    ReferencesIndustryTranslation translation =
                            translationRepository.findByIndustryAndLanguage(industry, language);

                    List<ReferencesCompany> companies = companyRepository.findByIndustry(industry);

                    IndustryDTO dto = new IndustryDTO();
                    dto.setId(industry.getId());
                    dto.setName(translation != null ? translation.getName() : "");
                    dto.setCompanies(companies.stream().map(c -> {
                        CompanyDTO cDto = new CompanyDTO();
                        cDto.setId(c.getId());
                        cDto.setName(c.getName());
                        return cDto;
                    }).collect(Collectors.toList()));

                    return dto;
                })
                .collect(Collectors.toList());
    }

    // 🎛️ Admin paneli için tüm endüstrileri getir (şirket filtresi olmadan)
    // Seçilen dilde çevirisi olan tüm endüstrileri döndürür (şirketi olsun olmasın)
    public List<IndustryDTO> getAllByLanguageCodeForAdmin(String code) {
        Language language = languageRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Dil bulunamadı: " + code));

        return industryRepository.findAll().stream()
                .filter(industry -> {
                    // Sadece çevirisi var mı kontrol et (şirket kontrolü yok)
                    ReferencesIndustryTranslation translation =
                            translationRepository.findByIndustryAndLanguage(industry, language);
                    return translation != null && translation.getName() != null && !translation.getName().trim().isEmpty();
                })
                .map(industry -> {
                    ReferencesIndustryTranslation translation =
                            translationRepository.findByIndustryAndLanguage(industry, language);

                    List<ReferencesCompany> companies = companyRepository.findByIndustry(industry);

                    IndustryDTO dto = new IndustryDTO();
                    dto.setId(industry.getId());
                    dto.setName(translation != null ? translation.getName() : "");
                    dto.setCompanies(companies.stream().map(c -> {
                        CompanyDTO cDto = new CompanyDTO();
                        cDto.setId(c.getId());
                        cDto.setName(c.getName());
                        return cDto;
                    }).collect(Collectors.toList()));

                    return dto;
                })
                .collect(Collectors.toList());
    }

    // 🧱 Industry ekle/güncelle
    public ReferencesIndustry addOrUpdateIndustry(Long id) {
        ReferencesIndustry industry = id != null
                ? industryRepository.findById(id).orElse(new ReferencesIndustry())
                : new ReferencesIndustry();

        return industryRepository.save(industry);
    }

    // 🌐 Industry çeviri ekle/güncelle
    public ReferencesIndustryTranslation addOrUpdateIndustryTranslation(Long industryId, String languageCode, String name) {
        ReferencesIndustry industry = industryRepository.findById(industryId)
                .orElseThrow(() -> new RuntimeException("Industry bulunamadı: " + industryId));

        Language language = languageRepository.findByCode(languageCode)
                .orElseThrow(() -> new RuntimeException("Dil bulunamadı: " + languageCode));

        ReferencesIndustryTranslation translation =
                translationRepository.findByIndustryAndLanguage(industry, language);

        if (translation == null) translation = new ReferencesIndustryTranslation();

        translation.setIndustry(industry);
        translation.setLanguage(language);
        translation.setName(name);

        return translationRepository.save(translation);
    }

    // 🏢 Şirket ekle/güncelle
    public ReferencesCompany addOrUpdateCompany(Long id, Long industryId, String name) {
        ReferencesIndustry industry = industryRepository.findById(industryId)
                .orElseThrow(() -> new RuntimeException("Industry bulunamadı: " + industryId));

        ReferencesCompany company = id != null
                ? companyRepository.findById(id).orElse(new ReferencesCompany())
                : new ReferencesCompany();

        company.setIndustry(industry);
        company.setName(name);

        return companyRepository.save(company);
    }

    // 🗑️ Şirket sil
    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }

    // 🗑️ Industry sil — tüm dillerdeki çeviriler + şirketler dahil
    @Transactional
    public void deleteIndustry(Long industryId) {
        // 1️⃣ Bu industry’e bağlı tüm şirketleri sil
        companyRepository.deleteByIndustryId(industryId);

        // 2️⃣ Bu industry’ye bağlı tüm translation kayıtlarını sil
        translationRepository.deleteByIndustryId(industryId);

        // 3️⃣ Son olarak industry kaydını sil
        industryRepository.deleteById(industryId);
    }
}
