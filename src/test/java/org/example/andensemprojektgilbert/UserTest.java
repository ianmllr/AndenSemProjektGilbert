package org.example.andensemprojektgilbert;

import org.example.andensemprojektgilbert.Infrastructure.UserRepo;
import org.example.andensemprojektgilbert.Model.User;
import org.example.andensemprojektgilbert.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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
        user.setEmail("test@com");
        boolean result = userService.register(user);
        assertTrue(result);
    }

    @Test
    void testLoginUser() {
        User user = userService.login("test@com", "123");
        System.out.println(user);
        assertEquals("Test", user.getName());
    }
    @Test
    void testUpdateUser() {
        User user = userService.login("test@com", "123");
        System.out.println(user);
        user.setName("TestName");
        boolean updated = userService.updateUser(user);
        assertTrue(updated);
    }
}
