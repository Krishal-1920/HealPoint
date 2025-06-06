package com.example.HealPoint.controller;

import com.example.HealPoint.model.RoleModel;
import com.example.HealPoint.service.RoleService;
import com.example.HealPoint.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/role")
public class RoleController {

    private final JwtUtil jwtUtil;

    private final RoleService roleService;

    @GetMapping("/getRoles")
    public ResponseEntity<List<RoleModel>> getRoles(@RequestHeader("Authorization") String tokenHeader){
        String authenticatedEmail = jwtUtil.extractUsername(tokenHeader);
        return ResponseEntity.ok(roleService.getRoles());
    }

}
