package com.example.HealPoint.controller;

import com.example.HealPoint.model.InventoryModel;
import com.example.HealPoint.service.InventoryService;
import com.example.HealPoint.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    private final JwtUtil jwtUtil;

    @PostMapping("/addProduct")
    public ResponseEntity<InventoryModel> addProduct(@RequestHeader("Authorization") String tokenHeader,
                                                     @RequestBody InventoryModel inventoryModel){
        String authenticatedEmail = jwtUtil.extractUsername(tokenHeader);
        return ResponseEntity.ok(inventoryService.addProduct(inventoryModel));
    }

    @GetMapping("/getProduct")
    public ResponseEntity<List<InventoryModel>> getProducts(@RequestHeader("Authorization") String tokenHeader,
                                                            @RequestParam(required = false) String search){
        String authenticatedEmail = jwtUtil.extractUsername(tokenHeader);
        return ResponseEntity.ok(inventoryService.getProducts(search));
    }

    @PutMapping("/updateProduct")
    public ResponseEntity<InventoryModel> updateProduct(@RequestHeader("Authorization") String tokenHeader,
                                                        @RequestParam String itemId,
                                                        @RequestBody InventoryModel inventoryModel){
        String authenticatedEmail = jwtUtil.extractUsername(tokenHeader);
        return ResponseEntity.ok(inventoryService.updateProduct(itemId, inventoryModel));
    }

    @DeleteMapping("/deleteProduct")
    public ResponseEntity<String> deleteProduct(@RequestParam String itemId){
        String message = inventoryService.deleteProduct(itemId);
        return ResponseEntity.ok(message);
    }
}
