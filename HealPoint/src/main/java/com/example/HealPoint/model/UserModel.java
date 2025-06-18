package com.example.HealPoint.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class UserModel {

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String username;
    @NotBlank
    @Email
    private String email;
    private String password;
    private String dateOfBirth;
    private String phoneNumber;
    private String gender;
    private String specialist;
    private String address;
    private List<RoleModel> roles;

}
