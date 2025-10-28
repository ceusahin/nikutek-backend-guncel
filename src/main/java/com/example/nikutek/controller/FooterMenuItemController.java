package com.example.nikutek.controller;

import com.example.nikutek.dto.FooterMenuItemDTO;
import com.example.nikutek.entity.FooterMenuItem;
import com.example.nikutek.service.FooterMenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/footer-menu-item")
@RequiredArgsConstructor
public class FooterMenuItemController {

    private final FooterMenuItemService itemService;

    @PostMapping("/{menuId}")
    public ResponseEntity<FooterMenuItemDTO> addItem(@PathVariable Long menuId, @RequestBody FooterMenuItem item) {
        FooterMenuItem savedItem = itemService.addItemToMenu(menuId, item);
        return ResponseEntity.ok(itemService.toDto(savedItem));
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<FooterMenuItemDTO> updateItem(@PathVariable Long itemId, @RequestBody FooterMenuItem item) {
        FooterMenuItem updatedItem = itemService.updateItem(itemId, item);
        return ResponseEntity.ok(itemService.toDto(updatedItem));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long itemId) {
        itemService.deleteItem(itemId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-menu/{menuId}")
    public ResponseEntity<List<FooterMenuItemDTO>> getItemsByMenuId(@PathVariable Long menuId) {
        List<FooterMenuItemDTO> dtos = itemService.getItemDTOsByMenuId(menuId);
        return ResponseEntity.ok(dtos);
    }
}
