package com.example.HealPoint.config;

import com.example.HealPoint.filter.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SpringSecurity {

    private final JwtFilter jwtFilter;

    public SpringSecurity(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/users/signup", "/users/login").permitAll()
                    .requestMatchers("/role/getRoles","/users/getAllUsers", "/inventory/addProduct", "/inventory/updateProduct", "/inventory/deleteProduct").hasRole("ADMIN")
                    .requestMatchers("/slots/createSlot", "/slots/deleteSlot", "/slots/updateSlot" ).hasRole("ADMIN")
                    .requestMatchers("/users/deleteAccount", "/users/updateProfile", "/inventory/getProduct", "/slots/getAllSlots").authenticated()
                    .requestMatchers("/cartItems/addToCart", "/cartItems/removeFromCart", "/cartItems/getCartItems").authenticated()
                    .requestMatchers("/billing/generateBill", "/billing/orderHistory").authenticated()
                    .requestMatchers("/appointment/bookAnAppointment", "/appointment/updateSlot", "/appointment/getAllBookings").authenticated()
                    .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
