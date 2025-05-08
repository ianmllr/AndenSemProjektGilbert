package org.example.andensemprojektgilbert;

import org.example.andensemprojektgilbert.Infrastructure.UserRepo;
import org.example.andensemprojektgilbert.Model.User;
import org.example.andensemprojektgilbert.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;



    @Test
    void newUser() {
        User user = new User();
        user.setName("Test");
        user.setPassword("123");
        user.setEmail("test@.com");
        boolean result = userService.register(user);
        assertTrue(result);
    }
}
