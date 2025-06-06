package com.example.HealPoint.controller;

import com.example.HealPoint.entity.User;
import com.example.HealPoint.entity.UserRole;
import com.example.HealPoint.model.UserModel;
import com.example.HealPoint.repository.UserRepository;
import com.example.HealPoint.repository.UserRoleRepository;
import com.example.HealPoint.service.UserService;
import com.example.HealPoint.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    private final UserRepository userRepository;

    private final UserRoleRepository userRoleRepository;

    @PostMapping("/signup")
    public ResponseEntity<UserModel> signUp(@RequestBody UserModel userModel) {
        return ResponseEntity.ok(userService.signUp(userModel));
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserModel>> getAllUsers(@RequestHeader("Authorization") String tokenHeader,
                                                       @RequestParam String search){
        String authenticatedEmail = jwtUtil.extractUsername(tokenHeader);
        return ResponseEntity.ok(userService.getAllUsers(search));
    }

    @DeleteMapping("/deleteAccount")
    public ResponseEntity<String> deleteUser(@RequestHeader("Authorization") String tokenHeader){
        String authenticatedEmail = jwtUtil.extractUsername(tokenHeader);
        return ResponseEntity.ok(userService.deleteUser(authenticatedEmail));
    }

    @PutMapping("/updateProfile")
    public ResponseEntity<UserModel> updateProfile(@RequestHeader("Authorization") String tokenHeader,
                                                   @RequestBody UserModel userModel){
        String authenticatedEmail = jwtUtil.extractUsername(tokenHeader);
        return ResponseEntity.ok(userService.updateProfile(authenticatedEmail, userModel));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserModel userModel) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userModel.getEmail(), userModel.getPassword()));

            User user = userRepository.findByEmail(userModel.getEmail());
            List<UserRole> roles = userRoleRepository.findByUserUserId(user.getUserId());
            List<String> roleNames = roles.stream()
                    .map(role -> role.getRole().getRoleName())
                    .toList();

            String jwt = jwtUtil.generateToken(user.getEmail(), roleNames);
            return ResponseEntity.ok(jwt);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect Email or Password");
        }
    }
}
