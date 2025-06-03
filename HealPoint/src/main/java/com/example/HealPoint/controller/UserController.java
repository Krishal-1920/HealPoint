package com.example.HealPoint.controller;

import com.example.HealPoint.model.UserModel;
import com.example.HealPoint.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserModel> signUp(@RequestBody UserModel userModel) {
        return ResponseEntity.ok(userService.signUp(userModel));
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserModel>> getAllUsers(@RequestParam String search){
        return ResponseEntity.ok(userService.getAllUsers(search));
    }

    @DeleteMapping("/deleteAccount")
    public ResponseEntity<String> deleteUser(@RequestParam String userId){
        return ResponseEntity.ok(userService.deleteUser(userId));
    }

    @PutMapping("/updateUser/{userId}")
    public ResponseEntity<UserModel> updateUser(@PathVariable String userId, @RequestBody UserModel userModel) {
        UserModel updatedUser = userService.updateUser(userId, userModel);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

}
