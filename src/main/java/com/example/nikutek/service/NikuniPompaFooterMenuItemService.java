package com.example.nikutek.service;

import com.example.nikutek.dto.FooterMenuItemDTO;
import com.example.nikutek.entity.NikuniPompaFooterMenu;
import com.example.nikutek.entity.NikuniPompaFooterMenuItem;
import com.example.nikutek.repository.NikuniPompaFooterMenuItemRepository;
import com.example.nikutek.repository.NikuniPompaFooterMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NikuniPompaFooterMenuItemService {

    private final NikuniPompaFooterMenuItemRepository itemRepository;
    private final NikuniPompaFooterMenuRepository menuRepository;

    public NikuniPompaFooterMenuItem addItemToMenu(Long menuId, NikuniPompaFooterMenuItem item) {
        NikuniPompaFooterMenu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("Menu not found"));
        item.setMenu(menu);
        return itemRepository.save(item);
    }

    public NikuniPompaFooterMenuItem updateItem(Long itemId, NikuniPompaFooterMenuItem updatedItem) {
        NikuniPompaFooterMenuItem existingItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        existingItem.setName(updatedItem.getName());
        existingItem.setUrl(updatedItem.getUrl());
        return itemRepository.save(existingItem);
    }

    public void deleteItem(Long itemId) {
        itemRepository.deleteById(itemId);
    }

    public List<NikuniPompaFooterMenuItem> getItemsByMenuId(Long menuId) {
        return itemRepository.findByMenuId(menuId);
    }

    public FooterMenuItemDTO toDto(NikuniPompaFooterMenuItem item) {
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

