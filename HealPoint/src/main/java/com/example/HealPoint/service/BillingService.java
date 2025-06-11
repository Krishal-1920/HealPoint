package com.example.HealPoint.service;

import com.example.HealPoint.entity.*;
import com.example.HealPoint.exceptions.DataNotFoundException;
import com.example.HealPoint.exceptions.DataValidationException;
import com.example.HealPoint.mapper.BillingMapper;
import com.example.HealPoint.model.BillingItemsModel;
import com.example.HealPoint.model.BillingModel;
import com.example.HealPoint.model.OrderHistoryDateModel;
import com.example.HealPoint.model.OrderHistoryModel;
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

    private final BillingMapper billingMapper;


    @Transactional
    public BillingModel generateBill(String email) {

        User user = userRepository.findByEmail(email);

        List<Cart> cartItems = cartRepository.findByUserUserId(user.getUserId());
        if (cartItems.isEmpty()) {
            throw new DataValidationException("Cart is empty");
        }

        Billing billing = new Billing();
        billing.setUser(user);
        billing.setBillingDate(LocalDate.now());

        List<BillingItem> billingItemsList = new ArrayList<>();
        List<BillingItemsModel> itemResponses = new ArrayList<>();
        double totalBillAmount = 0;

        for (Cart cart : cartItems) {
            BillingItem billingItem = billingMapper.updateToBillingItem(cart, billing);
            BillingItemsModel itemResponse = billingMapper.cartToBillingItemsModel(cart);

            billingItemsList.add(billingItem);
            itemResponses.add(itemResponse);
            totalBillAmount += itemResponse.getTotalProductPrice();
        }

        billing.setBillingItems(billingItemsList);
        billing.setTotalAmount(totalBillAmount);
        billingRepository.save(billing);
        cartRepository.deleteAll(cartItems);

        BillingModel response = new BillingModel();
        response.setUsername(user.getFirstName() + " " + user.getLastName());
        response.setDate(LocalDate.now());
        response.setAddress(user.getAddress());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setBillingItemsModels(itemResponses);
        response.setTotalPrice(totalBillAmount);

        return response;
    }

    public OrderHistoryModel getOrderHistory(String email) {
        User user = userRepository.findByEmail(email);

        List<Billing> billingList = billingRepository.findByUserUserId(user.getUserId());

        List<OrderHistoryDateModel> orderHistoryDateList = new ArrayList<>();

        for (Billing billing : billingList) {
            List<BillingItemsModel> billingItems = billing.getBillingItems().stream()
                    .map(item -> billingMapper.billingItemtoBillingItemsModel(item))
                    .toList();

            OrderHistoryDateModel dateModel = new OrderHistoryDateModel();
            dateModel.setBillDate(billing.getBillingDate());
            dateModel.setBillingItemsModel(billingItems);
            dateModel.setTotalBill(String.valueOf(billing.getTotalAmount()));

            orderHistoryDateList.add(dateModel);
        }

        OrderHistoryModel response = new OrderHistoryModel();
        response.setUsername(user.getUsername());
        response.setOrderHistoryDateModel(orderHistoryDateList);

        return response;
    }

//    public OrderHistoryModel getOrderHistory(String userId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        List<Billing> billingList = billingRepository.findByUserUserId(userId);
//
//        List<OrderHistoryDateModel> orderHistoryDateList = new ArrayList<>();
//
//        for (Billing billing : billingList) {
//            List<BillingItemsModel> billingItems = billing.getBillingItems().stream()
//                    .map(item -> {
//                        BillingItemsModel model = new BillingItemsModel();
//                        model.setProductName(item.getInventory().getProductName());
//                        model.setProductQuantity(item.getQuantity());
//                        model.setTotalProductPrice(item.getTotalPrice());
//                        return model;
//                    })
//                    .collect(Collectors.toList());
//
//            OrderHistoryDateModel dateModel = new OrderHistoryDateModel();
//            dateModel.setBillDate(billing.getBillingDate());
//            dateModel.setBillingItemsModel(billingItems);
//
//            orderHistoryDateList.add(dateModel);
//        }
//
//        OrderHistoryModel response = new OrderHistoryModel();
//        response.setUsername(user.getUsername());
//        response.setOrderHistoryDateModel(orderHistoryDateList);
//
//        return response;
//    }


}
