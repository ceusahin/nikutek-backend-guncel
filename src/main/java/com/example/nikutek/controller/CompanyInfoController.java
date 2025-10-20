package com.example.nikutek.controller;

import com.example.nikutek.dto.CompanyInfoDTO;
import com.example.nikutek.service.CompanyInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company-info")
@RequiredArgsConstructor
public class CompanyInfoController {

    private final CompanyInfoService service;

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
