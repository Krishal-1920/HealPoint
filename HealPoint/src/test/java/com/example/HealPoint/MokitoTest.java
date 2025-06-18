package com.example.HealPoint;

import com.example.HealPoint.entity.User;
import com.example.HealPoint.repository.UserRepository;
import com.example.HealPoint.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MokitoTest {

    // Def : A fake or dummy object that stands in for a real object

    @Mock
    UserRepository repo;

    @InjectMocks
    UserServiceTest service;

//    @Test
//    void testGetUserByEmail() {
//        // Setup mock user
//        User user = new User();
//        user.setUserId("8622d763-b49d-473f-8fd4-4353227bb35f");
//        user.setEmail("krish@gmail.com");
//
//        // Stub the repository
//        when(repo.findByEmail("krish@gmail.com")).thenReturn(user);
//
//        // Call the service
//        User result = service.getUserById("krish@gmail.com");
//
//        // Assertions
//        assertEquals(user, result);
//
//        // Verify interaction
//        verify(repo).findByEmail("krish@gmail.com");
//    }


}
