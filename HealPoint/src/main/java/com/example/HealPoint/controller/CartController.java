package com.example.HealPoint.controller;

import com.example.HealPoint.model.CartModel;
import com.example.HealPoint.model.MessageModel;
import com.example.HealPoint.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cartItems")
public class CartController {

    private final CartService cartService;

    @PostMapping("/addToCart")
    public ResponseEntity<MessageModel> addProductToCart(@RequestParam String userId,
                                                         @RequestParam String itemId,
                                                         @RequestParam double quantity) {
        return ResponseEntity.ok(cartService.addToCart(userId, itemId, quantity));
    }

    @DeleteMapping("/removeFromCart")
    public ResponseEntity<MessageModel> removeProductFromCart(@RequestParam String userId,
                                                              @RequestParam String itemId,
                                                              @RequestParam double quantity) {
        return ResponseEntity.ok(cartService.removeFromCart(userId, itemId, quantity));
    }

    @GetMapping("/getCartItems")
    public ResponseEntity<CartModel> getCartItems(@RequestParam String userId) {
        return ResponseEntity.ok(cartService.getCartItems(userId));
    }

}
