package com.example.HealPoint.controller;

import com.example.HealPoint.model.SpecialistModel;
import com.example.HealPoint.service.SpecialistService;
import com.example.HealPoint.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/specialist")
public class SpecialistController {

    private final SpecialistService specialistService;

    private final JwtUtil jwtUtil;

    @GetMapping("/getSpeciality")
    public ResponseEntity<List<SpecialistModel>> getSpeciality(@RequestParam(required = false) String search,
                                                               @RequestHeader("Authorization") String tokenHeader) {
        String authenticatedEmail = jwtUtil.extractUsername(tokenHeader);
        return ResponseEntity.ok(specialistService.getSpeciality(search));
    }

}
