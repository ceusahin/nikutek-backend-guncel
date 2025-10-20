package com.example.nikutek.service;

import com.example.nikutek.dto.ContactInfoDTO;
import com.example.nikutek.dto.ContactInfoTranslationDTO;
import com.example.nikutek.entity.ContactInfo;
import com.example.nikutek.entity.ContactInfoTranslation;
import com.example.nikutek.entity.Language;
import com.example.nikutek.repository.ContactInfoRepository;
import com.example.nikutek.repository.LanguageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactInfoService {

    private final ContactInfoRepository contactInfoRepository;
    private final LanguageRepository languageRepository;

    public ContactInfoService(ContactInfoRepository contactInfoRepository, LanguageRepository languageRepository) {
        this.contactInfoRepository = contactInfoRepository;
        this.languageRepository = languageRepository;
    }

    public List<ContactInfoDTO> getAll() {
        return contactInfoRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ContactInfoDTO saveOrUpdate(ContactInfoDTO dto) {
        ContactInfo entity = dto.getId() != null
                ? contactInfoRepository.findById(dto.getId()).orElse(new ContactInfo())
                : new ContactInfo();

        entity.setType(dto.getType());
        entity.setIsActive(dto.getIsActive());

        // Clear old translations if updating
        entity.getTranslations().clear();

        for (ContactInfoTranslationDTO transDto : dto.getTranslations()) {
            Language lang = languageRepository.findByCode(transDto.getLanguageCode())
                    .orElseThrow(() -> new RuntimeException("Language not found: " + transDto.getLanguageCode()));

            ContactInfoTranslation translation = new ContactInfoTranslation();
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

    private ContactInfoDTO mapToDTO(ContactInfo entity) {
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
