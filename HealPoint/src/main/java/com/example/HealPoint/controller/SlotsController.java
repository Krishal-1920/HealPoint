package com.example.HealPoint.controller;

import com.example.HealPoint.model.SlotsModel;
import com.example.HealPoint.service.SlotsService;
import com.example.HealPoint.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/slots")
public class SlotsController {

    private final SlotsService slotsService;

    private final JwtUtil jwtUtil;

    @PostMapping("/createSlot")
    public ResponseEntity<SlotsModel> createSlot(@RequestHeader("Authorization") String tokenHeader,
                                                 @RequestBody SlotsModel slotsModel) {
        String authenticatedEmail = jwtUtil.extractUsername(tokenHeader);
        return ResponseEntity.ok(slotsService.createSlot(slotsModel));
    }

    @GetMapping("/getAllSlots")
    public ResponseEntity<List<SlotsModel>> getAllSlots(@RequestHeader("Authorization") String tokenHeader,
                                                        @RequestParam String search) {
        String authenticatedEmail = jwtUtil.extractUsername(tokenHeader);
        return ResponseEntity.ok(slotsService.getAllSlots(search));
    }

    @DeleteMapping("/deleteSlot")
    public ResponseEntity<String> deleteSlot(@RequestHeader("Authorization") String tokenHeader,
                                             @RequestParam String slotId) {
        String authenticatedEmail = jwtUtil.extractUsername(tokenHeader);
        String message = slotsService.deleteSlot(slotId);
        return ResponseEntity.ok(message);
    }

    @PutMapping("/updateSlot")
    public ResponseEntity<SlotsModel> updateSlot(@RequestHeader("Authorization") String tokenHeader,
                                                 @RequestParam String slotId,
                                                 @RequestBody SlotsModel slotsModel) {
        String authenticatedEmail = jwtUtil.extractUsername(tokenHeader);
        return ResponseEntity.ok(slotsService.updateSlot(slotId, slotsModel));
    }
}
