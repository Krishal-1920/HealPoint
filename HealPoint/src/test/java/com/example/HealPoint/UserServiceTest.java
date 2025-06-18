package com.example.HealPoint;

import com.example.HealPoint.entity.User;
import com.example.HealPoint.repository.UserRepository;
import org.mockito.Mock;
import org.springframework.stereotype.Service;

@Service
public class UserServiceTest {

    @Mock
    UserRepository repository;

    public User getUserById(String email) {
        return repository.findByEmail(email);

    }

}
