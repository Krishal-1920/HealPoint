package com.example.HealPoint.controller;

import com.example.HealPoint.model.CartModel;
import com.example.HealPoint.model.MessageModel;
import com.example.HealPoint.service.CartService;
import com.example.HealPoint.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cartItems")
public class CartController {

    private final CartService cartService;

    private final JwtUtil jwtUtil;

    @PostMapping("/addToCart")
    public ResponseEntity<MessageModel> addProductToCart(@RequestHeader("Authorization") String tokenHeader,
                                                         @RequestParam String itemId,
                                                         @RequestParam double quantity) {
        String authenticatedEmail = jwtUtil.extractUsername(tokenHeader);
        return ResponseEntity.ok(cartService.addToCart(authenticatedEmail, itemId, quantity));
    }

    @DeleteMapping("/removeFromCart")
    public ResponseEntity<MessageModel> removeProductFromCart(@RequestHeader("Authorization") String tokenHeader,
                                                              @RequestParam String itemId,
                                                              @RequestParam double quantity) {
        String authenticatedEmail = jwtUtil.extractUsername(tokenHeader);
        return ResponseEntity.ok(cartService.removeFromCart(authenticatedEmail, itemId, quantity));
    }

    @GetMapping("/getCartItems")
    public ResponseEntity<CartModel> getCartItems(@RequestHeader("Authorization") String tokenHeader) {
        String authenticatedEmail = jwtUtil.extractUsername(tokenHeader);
        return ResponseEntity.ok(cartService.getCartItems(authenticatedEmail));
    }

}
