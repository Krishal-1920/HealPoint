package com.example.HealPoint.controller;

import com.example.HealPoint.model.InventoryModel;
import com.example.HealPoint.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("/addProduct")
    public ResponseEntity<InventoryModel> addProduct(@RequestBody InventoryModel inventoryModel){
        return ResponseEntity.ok(inventoryService.addProduct(inventoryModel));
    }

    @GetMapping("/getProduct")
    public ResponseEntity<List<InventoryModel>> getProducts(@RequestParam(required = false) String search){
        return ResponseEntity.ok(inventoryService.getProducts(search));
    }

    @PutMapping("/updateProduct")
    public ResponseEntity<InventoryModel> updateProduct(@RequestParam String itemId,
                                                        @RequestBody InventoryModel inventoryModel){
        return ResponseEntity.ok(inventoryService.updateProduct(itemId, inventoryModel));
    }

    @DeleteMapping("/deleteProduct")
    public ResponseEntity<String> deleteProduct(@RequestParam String itemId){
        String message = inventoryService.deleteProduct(itemId);
        return ResponseEntity.ok(message);
    }
}
