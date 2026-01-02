package com.example.nikutek.service;

import com.example.nikutek.dto.ExperienceDTO;
import com.example.nikutek.dto.ExperienceTranslationDTO;
import com.example.nikutek.entity.NikuniPompaExperience;
import com.example.nikutek.entity.NikuniPompaExperienceTranslation;
import com.example.nikutek.entity.Language;
import com.example.nikutek.repository.NikuniPompaExperienceRepository;
import com.example.nikutek.repository.NikuniPompaExperienceTranslationRepository;
import com.example.nikutek.repository.LanguageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NikuniPompaExperienceServiceImpl implements NikuniPompaExperienceService {

    private final NikuniPompaExperienceRepository experienceRepository;
    private final NikuniPompaExperienceTranslationRepository translationRepository;
    private final LanguageRepository languageRepository;

    public NikuniPompaExperienceServiceImpl(NikuniPompaExperienceRepository experienceRepository,
                                 NikuniPompaExperienceTranslationRepository translationRepository,
                                 LanguageRepository languageRepository) {
        this.experienceRepository = experienceRepository;
        this.translationRepository = translationRepository;
        this.languageRepository = languageRepository;
    }

    @Override
    public List<ExperienceDTO> getAll() {
        return experienceRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ExperienceDTO saveOrUpdate(ExperienceDTO dto) {
        NikuniPompaExperience experience = dto.getId() != null ?
                experienceRepository.findById(dto.getId()).orElse(new NikuniPompaExperience()) :
                new NikuniPompaExperience();

        experience.setVisible(dto.isVisible());

        NikuniPompaExperience saved = experienceRepository.save(experience);

        translationRepository.deleteAll(translationRepository.findByExperienceId(saved.getId()));

        List<NikuniPompaExperienceTranslation> translations = dto.getTranslations().stream().map(t -> {
            Language langEntity = languageRepository.findByCode(t.getLanguage())
                    .orElseThrow(() -> new RuntimeException("Dil bulunamadı: " + t.getLanguage()));

            NikuniPompaExperienceTranslation et = new NikuniPompaExperienceTranslation();
            et.setLanguage(langEntity);
            et.setNumberText(t.getNumberText());
            et.setLabelText(t.getLabelText());
            et.setExperience(saved);
            return et;
        }).collect(Collectors.toList());

        translationRepository.saveAll(translations);

        return mapToDTO(saved);

    }

    @Override
    public void delete(Long id) {
        translationRepository.deleteAll(translationRepository.findByExperienceId(id));
        experienceRepository.deleteById(id);
    }

    private ExperienceDTO mapToDTO(NikuniPompaExperience experience) {
        List<ExperienceTranslationDTO> translations = experience.getTranslations().stream()
                .map(t -> new ExperienceTranslationDTO(
                        t.getLanguage().getCode(),
                        t.getNumberText(),
                        t.getLabelText()
                ))
                .collect(Collectors.toList());

        return new ExperienceDTO(
                experience.getId(),
                experience.isVisible(),
                translations
        );
    }
}

