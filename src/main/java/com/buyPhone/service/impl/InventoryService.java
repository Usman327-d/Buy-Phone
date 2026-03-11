package com.buyPhone.service.impl;

import com.buyPhone.entity.Inventory;
import com.buyPhone.exception.InventoryException;
import com.buyPhone.exception.ResourceNotFoundException;
import com.buyPhone.repository.InventoryRepository;
import com.buyPhone.service.interfac.IInventoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class InventoryService implements IInventoryService {

    private final InventoryRepository repository;


    @Override
    @Transactional
    public void decreaseStock(UUID productId, int quantity) {
        Inventory inventory = repository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory", productId.toString()));
        if(inventory.getQuantity() < quantity)
            throw new InventoryException("Insufficient stock for product: " + productId);
        inventory.setQuantity(inventory.getQuantity() - quantity);
        repository.save(inventory);
    }

    @Override
    @Transactional
    public void increaseStock(UUID productId, int quantity) {
        Inventory inventory = repository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory", productId.toString()));
        inventory.setQuantity(inventory.getQuantity() + quantity);
        repository.save(inventory);
    }
}
