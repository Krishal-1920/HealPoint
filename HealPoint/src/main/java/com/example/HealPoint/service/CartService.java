package com.example.HealPoint.service;

import com.example.HealPoint.entity.Cart;
import com.example.HealPoint.entity.Inventory;
import com.example.HealPoint.entity.User;
import com.example.HealPoint.exceptions.DataNotFoundException;
import com.example.HealPoint.exceptions.DataValidationException;
import com.example.HealPoint.mapper.CartMapper;
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

    private final CartMapper cartMapper;

    public MessageModel addToCart(String email, String itemId, double quantity) {

        User user = userRepository.findByEmail(email);

        Inventory inventory = inventoryRepository.findById(itemId)
                .orElseThrow(() -> new DataNotFoundException("Item not found"));

        if (quantity > inventory.getItemQuantity()) {
            throw new DataValidationException("Quantity not available");
        }

        inventory.setItemQuantity(inventory.getItemQuantity() - quantity);
        inventoryRepository.save(inventory);

        Cart cart = cartRepository.findByUserUserIdAndInventoryItemId(user.getUserId(), itemId);

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


    public MessageModel removeFromCart(String email, String itemId, double quantity) {

        User user = userRepository.findByEmail(email);

        Inventory inventory = inventoryRepository.findById(itemId)
              .orElseThrow(() -> new DataNotFoundException("Item not found"));

        Cart cart = cartRepository.findByUserUserIdAndInventoryItemId(user.getUserId(), itemId);

        if(quantity > cart.getQuantity()){
             throw new DataValidationException("Quantity not available");
        }

        cart.setQuantity(cart.getQuantity() - quantity);
        cartRepository.save(cart);
        inventory.setItemQuantity(inventory.getItemQuantity() + quantity);
        inventoryRepository.save(inventory);

        MessageModel messageModel = new MessageModel();
        messageModel.setMessage(quantity + " " + inventory.getItemCategory().toString().toLowerCase() + " of " + inventory.getProductName() + " removed from cart");
        return messageModel;

    }

    public CartModel getCartItems(String email) {
        User user = userRepository.findByEmail(email);

        List<Cart> cart = cartRepository.findByUserUserId(user.getUserId());

        List<ItemListModel> cartModelList = cart.stream()
                .map(item -> cartMapper.cartToItemListModel(item))
                .toList();

        CartModel cartModel = new CartModel();
        cartModel.setUsername(user.getUsername());
        cartModel.setItemListModel(cartModelList);
        return cartModel;
    }



//    public CartModel getCartItems(String email) {
//        User user = userRepository.findByEmail(email);
//
//        List<Cart> cart = cartRepository.findByUserUserId(user.getUserId());
//
//        List<ItemListModel> cartModelList = cart.stream().map(cart1 -> {
//            ItemListModel itemListModel = new ItemListModel();
//            itemListModel.setProductName(cart1.getInventory().getProductName());
//            itemListModel.setItemCategory(cart1.getInventory().getItemCategory().toString());
//            itemListModel.setItemQuantity(String.valueOf(cart1.getQuantity()));
//            itemListModel.setItemPrice(String.valueOf(cart1.getInventory().getItemPrice()));
//            return itemListModel;
//        }).toList();
//        CartModel cartModel = new CartModel();
//        cartModel.setUsername(user.getUsername());
//        cartModel.setItemListModel(cartModelList);
//        return cartModel;
//    }

}
