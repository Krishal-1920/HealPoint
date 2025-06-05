package com.example.HealPoint.service;

import com.example.HealPoint.entity.Cart;
import com.example.HealPoint.entity.Inventory;
import com.example.HealPoint.entity.User;
import com.example.HealPoint.model.CartModel;
import com.example.HealPoint.model.ItemListModel;
import com.example.HealPoint.model.MessageModel;
import com.example.HealPoint.repository.CartRepository;
import com.example.HealPoint.repository.InventoryRepository;
import com.example.HealPoint.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

        if (quantity > inventory.getItemQuantity()) {
            throw new RuntimeException("Quantity not available");
        }

        inventory.setItemQuantity(inventory.getItemQuantity() - quantity);
        inventoryRepository.save(inventory);

        Cart cart = cartRepository.findByUserUserIdAndInventoryItemId(userId, itemId);

        if (cart != null) {
            cart.setQuantity(cart.getQuantity() + quantity);
        } else {
            cart = new Cart();
            cart.setUser(user);
            cart.setInventory(inventory);
            cart.setQuantity(quantity);
        }

        cartRepository.save(cart);

        MessageModel messageModel = new MessageModel();
        messageModel.setMessage(quantity + " " + inventory.getItemCategory().toString().toLowerCase() + " of " + inventory.getProductName() + " added to cart");
        return messageModel;
    }


    public MessageModel removeFromCart(String userId, String itemId, double quantity) {

        User user = userRepository.findById(userId)
               .orElseThrow(() -> new RuntimeException("User not found"));

        Inventory inventory = inventoryRepository.findById(itemId)
              .orElseThrow(() -> new RuntimeException("Item not found"));

        Cart cart = cartRepository.findByUserUserIdAndInventoryItemId(userId, itemId);

        if(quantity > cart.getQuantity()){
             throw new RuntimeException("Quantity not available");
        }

        cart.setQuantity(cart.getQuantity() - quantity);
        cartRepository.save(cart);
        inventory.setItemQuantity(inventory.getItemQuantity() + quantity);
        inventoryRepository.save(inventory);

        MessageModel messageModel = new MessageModel();
        messageModel.setMessage(quantity + " " + inventory.getItemCategory().toString().toLowerCase() + " of " + inventory.getProductName() + " removed from cart");
        return messageModel;

    }

    public CartModel getCartItems(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Cart> cart = cartRepository.findByUserUserId(userId);

        List<ItemListModel> cartModelList = cart.stream().map(cart1 -> {
            ItemListModel itemListModel = new ItemListModel();
            itemListModel.setProductName(cart1.getInventory().getProductName());
            itemListModel.setItemCategory(cart1.getInventory().getItemCategory().toString());
            itemListModel.setItemQuantity(String.valueOf(cart1.getQuantity()));
            itemListModel.setItemPrice(String.valueOf(cart1.getInventory().getItemPrice()));
            return itemListModel;
        }).toList();
        CartModel cartModel = new CartModel();
        cartModel.setUsername(user.getUsername());
        cartModel.setItemListModel(cartModelList);
        return cartModel;
    }

}
