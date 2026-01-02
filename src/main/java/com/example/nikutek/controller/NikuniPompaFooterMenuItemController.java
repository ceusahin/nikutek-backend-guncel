package com.example.nikutek.controller;

import com.example.nikutek.dto.FooterMenuItemDTO;
import com.example.nikutek.entity.NikuniPompaFooterMenuItem;
import com.example.nikutek.service.NikuniPompaFooterMenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nikuni-pompa/footer-menu-item")
@RequiredArgsConstructor
public class NikuniPompaFooterMenuItemController {

    private final NikuniPompaFooterMenuItemService itemService;

    @PostMapping("/{menuId}")
    public ResponseEntity<FooterMenuItemDTO> addItem(@PathVariable Long menuId, @RequestBody NikuniPompaFooterMenuItem item) {
        NikuniPompaFooterMenuItem savedItem = itemService.addItemToMenu(menuId, item);
        return ResponseEntity.ok(itemService.toDto(savedItem));
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<FooterMenuItemDTO> updateItem(@PathVariable Long itemId, @RequestBody NikuniPompaFooterMenuItem item) {
        NikuniPompaFooterMenuItem updatedItem = itemService.updateItem(itemId, item);
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

