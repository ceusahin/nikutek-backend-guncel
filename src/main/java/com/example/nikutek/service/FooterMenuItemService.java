package com.example.nikutek.service;

import com.example.nikutek.dto.FooterMenuItemDTO;
import com.example.nikutek.entity.FooterMenu;
import com.example.nikutek.entity.FooterMenuItem;
import com.example.nikutek.repository.FooterMenuItemRepository;
import com.example.nikutek.repository.FooterMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FooterMenuItemService {

    private final FooterMenuItemRepository itemRepository;
    private final FooterMenuRepository menuRepository;

    public FooterMenuItem addItemToMenu(Long menuId, FooterMenuItem item) {
        FooterMenu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("Menu not found"));
        item.setMenu(menu);
        return itemRepository.save(item);
    }

    public FooterMenuItem updateItem(Long itemId, FooterMenuItem updatedItem) {
        FooterMenuItem existingItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        existingItem.setName(updatedItem.getName());
        existingItem.setUrl(updatedItem.getUrl());
        return itemRepository.save(existingItem);
    }

    public void deleteItem(Long itemId) {
        itemRepository.deleteById(itemId);
    }

    public List<FooterMenuItem> getItemsByMenuId(Long menuId) {
        return itemRepository.findByMenuId(menuId);
    }

    public FooterMenuItemDTO toDto(FooterMenuItem item) {
        FooterMenuItemDTO dto = new FooterMenuItemDTO();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setUrl(item.getUrl());
        return dto;
    }

    public List<FooterMenuItemDTO> getItemDTOsByMenuId(Long menuId) {
        return getItemsByMenuId(menuId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
