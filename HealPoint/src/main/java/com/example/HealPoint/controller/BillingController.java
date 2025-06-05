package com.example.HealPoint.controller;

import com.example.HealPoint.model.BillingModel;
import com.example.HealPoint.service.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/billing")
public class BillingController {

    private final BillingService billingService;

    @PostMapping("/generateBill")
    public ResponseEntity<BillingModel> generateBill(@RequestParam String userId){
        return ResponseEntity.ok(billingService.generateBill(userId));
    }


}
