package com.example.HealPoint.service;

import com.example.HealPoint.entity.Cart;
import com.example.HealPoint.entity.Inventory;
import com.example.HealPoint.entity.User;
import com.example.HealPoint.model.CartModel;
import com.example.HealPoint.model.MessageModel;
import com.example.HealPoint.repository.CartRepository;
import com.example.HealPoint.repository.InventoryRepository;
import com.example.HealPoint.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    private final UserRepository userRepository;

    private final InventoryRepository inventoryRepository;

    private final CartRepository cartRepository;

    public MessageModel addToCart(String userId, String itemId, double quantity) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Inventory inventory = inventoryRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if(quantity > inventory.getItemQuantity()){
            throw new RuntimeException("Quantity not available");
        }
        inventory.setItemQuantity(inventory.getItemQuantity() - quantity);

        inventoryRepository.save(inventory);

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setInventory(inventory);
        cart.setQuantity(quantity);

        cartRepository.save(cart);

        MessageModel messageModel = new MessageModel();
        messageModel.setMessage(cart.getQuantity() + " " + cart.getInventory().getProductName()  + " added to cart");
        return messageModel;

    }







}
