package com.example.nikutek.controller;

import com.example.nikutek.dto.CompanyInfoDTO;
import com.example.nikutek.service.NikuniPompaCompanyInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/nikuni-pompa/company-info")
@RequiredArgsConstructor
public class NikuniPompaCompanyInfoController {

    private final NikuniPompaCompanyInfoService service;

    @GetMapping("/{languageCode}")
    public CompanyInfoDTO getByLanguage(@PathVariable String languageCode) {
        return service.getByLanguageCode(languageCode);
    }

    @PostMapping
    public CompanyInfoDTO saveOrUpdate(@RequestBody CompanyInfoDTO dto) {
        return service.saveOrUpdate(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}

