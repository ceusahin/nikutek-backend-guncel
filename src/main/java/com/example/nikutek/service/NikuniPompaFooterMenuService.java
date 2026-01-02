package com.example.nikutek.service;

import com.example.nikutek.dto.FooterMenuDTO;
import com.example.nikutek.dto.FooterMenuItemDTO;
import com.example.nikutek.entity.NikuniPompaFooterMenu;
import com.example.nikutek.entity.NikuniPompaFooterMenuItem;
import com.example.nikutek.entity.Language;
import com.example.nikutek.repository.NikuniPompaFooterMenuRepository;
import com.example.nikutek.repository.LanguageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NikuniPompaFooterMenuService {

    private final NikuniPompaFooterMenuRepository menuRepository;
    private final LanguageRepository languageRepository;

    public List<NikuniPompaFooterMenu> getMenusByLanguageId(Long languageId) {
        return menuRepository.findByLanguage_Id(languageId);
    }

    public List<FooterMenuDTO> getMenuDTOsByLanguageCode(String code) {
        Language language = languageRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Language not found: " + code));

        List<NikuniPompaFooterMenu> menus = getMenusByLanguageId(language.getId());
        return menus.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public NikuniPompaFooterMenu saveMenu(NikuniPompaFooterMenu menu) {
        return menuRepository.save(menu);
    }

    public void deleteMenu(Long id) {
        menuRepository.deleteById(id);
    }

    public Optional<NikuniPompaFooterMenu> getMenu(Long id) {
        return menuRepository.findById(id);
    }

    public FooterMenuDTO toDto(NikuniPompaFooterMenu menu) {
        FooterMenuDTO dto = new FooterMenuDTO();
        dto.setId(menu.getId());
        dto.setLanguageId(menu.getLanguage().getId());
        dto.setTitle(menu.getTitle());

        List<FooterMenuItemDTO> itemDTOs = menu.getItems().stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        dto.setItems(itemDTOs);
        return dto;
    }

    public FooterMenuItemDTO toDto(NikuniPompaFooterMenuItem item) {
        FooterMenuItemDTO dto = new FooterMenuItemDTO();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setUrl(item.getUrl());
        return dto;
    }
}

