package com.example.nikutek.service;

import com.example.nikutek.dto.ContactInfoDTO;
import com.example.nikutek.dto.ContactInfoTranslationDTO;
import com.example.nikutek.entity.NikuniPompaContactInfo;
import com.example.nikutek.entity.NikuniPompaContactInfoTranslation;
import com.example.nikutek.entity.Language;
import com.example.nikutek.repository.NikuniPompaContactInfoRepository;
import com.example.nikutek.repository.LanguageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NikuniPompaContactInfoService {

    private final NikuniPompaContactInfoRepository contactInfoRepository;
    private final LanguageRepository languageRepository;

    public NikuniPompaContactInfoService(NikuniPompaContactInfoRepository contactInfoRepository, LanguageRepository languageRepository) {
        this.contactInfoRepository = contactInfoRepository;
        this.languageRepository = languageRepository;
    }

    public List<ContactInfoDTO> getAll() {
        return contactInfoRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ContactInfoDTO saveOrUpdate(ContactInfoDTO dto) {
        NikuniPompaContactInfo entity = dto.getId() != null
                ? contactInfoRepository.findById(dto.getId()).orElse(new NikuniPompaContactInfo())
                : new NikuniPompaContactInfo();

        entity.setType(dto.getType());
        entity.setIsActive(dto.getIsActive());

        // Clear old translations if updating
        entity.getTranslations().clear();

        for (ContactInfoTranslationDTO transDto : dto.getTranslations()) {
            Language lang = languageRepository.findByCode(transDto.getLanguageCode())
                    .orElseThrow(() -> new RuntimeException("Language not found: " + transDto.getLanguageCode()));

            NikuniPompaContactInfoTranslation translation = new NikuniPompaContactInfoTranslation();
            translation.setLanguage(lang);
            translation.setTitle(transDto.getTitle());
            translation.setContent(transDto.getContent());
            translation.setContactInfo(entity);
            entity.getTranslations().add(translation);
        }

        return mapToDTO(contactInfoRepository.save(entity));
    }

    public void delete(Long id) {
        contactInfoRepository.deleteById(id);
    }

    private ContactInfoDTO mapToDTO(NikuniPompaContactInfo entity) {
        ContactInfoDTO dto = new ContactInfoDTO();
        dto.setId(entity.getId());
        dto.setType(entity.getType());
        dto.setIsActive(entity.getIsActive());

        List<ContactInfoTranslationDTO> translations = entity.getTranslations().stream()
                .map(t -> {
                    ContactInfoTranslationDTO tdto = new ContactInfoTranslationDTO();
                    tdto.setLanguageCode(t.getLanguage().getCode());
                    tdto.setTitle(t.getTitle());
                    tdto.setContent(t.getContent());
                    return tdto;
                })
                .collect(Collectors.toList());

        dto.setTranslations(translations);
        return dto;
    }
}

