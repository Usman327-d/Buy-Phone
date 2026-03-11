package com.buyPhone.mapper;

import com.buyPhone.dto.InventoryDTO;
import com.buyPhone.entity.Inventory;

public class InventoryMapper {

    public InventoryDTO toDTO(Inventory inventory){
        if(inventory == null) return null;

        return InventoryDTO.builder()
                .productId(inventory.getProduct() != null ? inventory.getProduct().getId().toString() : null)
                .quantity(inventory.getQuantity())
                .build();
    }

    public Inventory toEntity(InventoryDTO dto){
        if(dto == null) return null;

        Inventory inventory = new Inventory();
        inventory.setQuantity(dto.getQuantity());
        // product should be set in service
        return inventory;
    }
}
