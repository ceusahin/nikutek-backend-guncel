package com.example.nikutek.controller;

import com.example.nikutek.dto.FooterMenuDTO;
import com.example.nikutek.entity.FooterMenu;
import com.example.nikutek.entity.Language;
import com.example.nikutek.repository.LanguageRepository;
import com.example.nikutek.service.FooterMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nikutek/footer-menu")
@RequiredArgsConstructor
public class FooterMenuController {

    private final FooterMenuService menuService;
    private final LanguageRepository languageRepository;

    // 🔹 Artık 'tr', 'en' gibi dil kodlarını alıyor
    @GetMapping("/{languageCode}")
    public ResponseEntity<List<FooterMenuDTO>> getMenusByLanguage(@PathVariable String languageCode) {
        List<FooterMenuDTO> dtos = menuService.getMenuDTOsByLanguageCode(languageCode);
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<FooterMenu> createMenu(@RequestBody FooterMenu menu, @RequestParam String languageCode) {
        Language language = languageRepository.findByCode(languageCode)
                .orElseThrow(() -> new RuntimeException("Language not found: " + languageCode));
        menu.setLanguage(language);
        return ResponseEntity.ok(menuService.saveMenu(menu));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FooterMenu> updateMenu(
            @PathVariable Long id,
            @RequestBody FooterMenu updatedMenu,
            @RequestParam(required = false) String languageCode
    ) {
        return menuService.getMenu(id)
                .map(menu -> {
                    updatedMenu.setId(id);

                    if (languageCode != null) {
                        Language language = languageRepository.findByCode(languageCode)
                                .orElseThrow(() -> new RuntimeException("Language not found: " + languageCode));
                        updatedMenu.setLanguage(language);
                    }

                    return ResponseEntity.ok(menuService.saveMenu(updatedMenu));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long id) {
        menuService.deleteMenu(id);
        return ResponseEntity.noContent().build();
    }
}
