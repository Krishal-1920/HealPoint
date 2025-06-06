package com.example.HealPoint.service;

import com.example.HealPoint.entity.*;
import com.example.HealPoint.exceptions.DataNotFoundException;
import com.example.HealPoint.exceptions.DataValidationException;
import com.example.HealPoint.model.BillingItemsModel;
import com.example.HealPoint.model.BillingModel;
import com.example.HealPoint.repository.BillingRepository;
import com.example.HealPoint.repository.CartRepository;
import com.example.HealPoint.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BillingService {

    private final UserRepository userRepository;

    private final CartRepository cartRepository;

    private final BillingRepository billingRepository;


    @Transactional
    public BillingModel generateBill(String userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        List<Cart> cartItems = cartRepository.findByUserUserId(userId);
        if (cartItems.isEmpty()) {
            throw new DataValidationException("Cart is empty");
        }

        Billing billing = new Billing();
        billing.setUser(user);
        billing.setBillingDate(LocalDate.now());

        List<BillingItem> billingItemsList = new ArrayList<>();
        List<BillingItemsModel> itemResponses = new ArrayList<>();
        double totalAmount = 0;

        for (Cart cart : cartItems) {
            Inventory inventory = cart.getInventory();
            double itemTotal = inventory.getItemPrice() * cart.getQuantity();

            BillingItem billingItem = new BillingItem();
            billingItem.setBilling(billing);
            billingItem.setInventory(inventory);
            billingItem.setProductName(inventory.getProductName());
            billingItem.setPriceAtPurchase(inventory.getItemPrice());
            billingItem.setQuantity(cart.getQuantity());
            billingItemsList.add(billingItem);

            BillingItemsModel itemResponse = new BillingItemsModel();
            itemResponse.setProductName(inventory.getProductName());
            itemResponse.setProductQuantity(cart.getQuantity());
            itemResponse.setTotalProductPrice(itemTotal);
            itemResponses.add(itemResponse);

            totalAmount = totalAmount + itemTotal;
        }

        billing.setBillingItems(billingItemsList);
        billing.setTotalAmount(totalAmount);
        billingRepository.save(billing);
        cartRepository.deleteAll(cartItems);

        BillingModel response = new BillingModel();
        response.setUsername(user.getFirstName() + " " + user.getLastName());
        response.setDate(LocalDate.now());
        response.setAddress(user.getAddress());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setBillingItemsModels(itemResponses);
        response.setTotalPrice(totalAmount);

        return response;
    }
}
