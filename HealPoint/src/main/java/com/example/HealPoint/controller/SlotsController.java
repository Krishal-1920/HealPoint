package com.example.HealPoint.controller;

import com.example.HealPoint.model.SlotsModel;
import com.example.HealPoint.service.SlotsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/slots")
public class SlotsController {

    private final SlotsService slotsService;

    @PostMapping("/createSlot")
    public ResponseEntity<SlotsModel> createSlot(@RequestBody SlotsModel slotsModel) {
        return ResponseEntity.ok(slotsService.createSlot(slotsModel));
    }

    @GetMapping("/getAllSlots")
    public ResponseEntity<List<SlotsModel>> getAllSlots(@RequestParam String search) {
        return ResponseEntity.ok(slotsService.getAllSlots(search));
    }

    @DeleteMapping("/deleteSlot")
    public ResponseEntity<String> deleteSlot(@RequestParam String slotId) {
        String message = slotsService.deleteSlot(slotId);
        return ResponseEntity.ok(message);
    }

    @PutMapping("/updateSlot")
    public ResponseEntity<SlotsModel> updateSlot(@RequestParam String slotId,
                                                 @RequestBody SlotsModel slotsModel) {
        return ResponseEntity.ok(slotsService.updateSlot(slotId, slotsModel));
    }
}
