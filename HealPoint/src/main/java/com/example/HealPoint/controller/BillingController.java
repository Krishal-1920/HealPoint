package com.example.HealPoint.controller;

import com.example.HealPoint.model.BillingModel;
import com.example.HealPoint.model.OrderHistoryModel;
import com.example.HealPoint.service.BillingService;
import com.example.HealPoint.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/billing")
public class BillingController {

    private final BillingService billingService;

    private final JwtUtil jwtUtil;


    @PostMapping("/generateBill")
    public ResponseEntity<BillingModel> generateBill(@RequestHeader("Authorization") String tokenHeader){
        String authenticatedEmail = jwtUtil.extractUsername(tokenHeader);
        return ResponseEntity.ok(billingService.generateBill(authenticatedEmail));
    }

    @GetMapping("/orderHistory")
    public ResponseEntity<OrderHistoryModel> getOrderHistory(@RequestHeader("Authorization") String tokenHeader){
        String authenticatedEmail = jwtUtil.extractUsername(tokenHeader);
        return ResponseEntity.ok(billingService.getOrderHistory(authenticatedEmail));
    }

}
