package com.example.HealPoint.service;

import com.example.HealPoint.entity.Inventory;
import com.example.HealPoint.exceptions.DataNotFoundException;
import com.example.HealPoint.mapper.InventoryMapper;
import com.example.HealPoint.model.InventoryModel;
import com.example.HealPoint.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    private final InventoryMapper inventoryMapper;
    
    public InventoryModel addProduct(InventoryModel inventoryModel) {
        Inventory inventory = inventoryMapper.inventoryModelToInventory(inventoryModel);
        inventory = inventoryRepository.save(inventory);
        return inventoryMapper.inventoryToInventoryModel(inventory);
    }


    public List<InventoryModel> getProducts(String search) {
        List<Inventory> inventoryList = inventoryRepository.searchList(search);

        return inventoryMapper.inventoryListToInventoryModelList(inventoryList);
    }


    public InventoryModel updateProduct(String itemId, InventoryModel inventoryModel) {
        Inventory inventory = inventoryRepository.findById(itemId)
                .orElseThrow(() -> new DataNotFoundException("Product not found"));

        inventoryMapper.updateInventoryFromInventoryModel(inventoryModel, inventory);
        inventory.setItemId(itemId);
        inventory = inventoryRepository.save(inventory);
        return inventoryMapper.inventoryToInventoryModel(inventory);
    }

    public String deleteProduct(String itemId) {
        Inventory inventory = inventoryRepository.findById(itemId)
                .orElseThrow(() -> new DataNotFoundException("Product not found"));
        inventoryRepository.delete(inventory);
        return "Product removed successfully";
    }
}
