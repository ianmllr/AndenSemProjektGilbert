package org.example.andensemprojektgilbert;

import org.example.andensemprojektgilbert.Infrastructure.UserRepo;
import org.example.andensemprojektgilbert.Model.User;
import org.example.andensemprojektgilbert.Service.IUserService;
import org.example.andensemprojektgilbert.Service.UserServiceImp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserTest {

    @Autowired
    private IUserService userService;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setName("Test");
        user.setPassword("123");
        user.setEmail("test@com");
        userService.register(user);
    }

    @Test
    void testLoginUser() {
        User user = userService.login("test@com", "123");
        assertEquals("Test", user.getName());
    }
    @Test
    void testUpdateUser() {
        User user = userService.login("test@com", "123");
        user.setName("TestName");
        boolean updated = userService.updateUser(user);
        assertTrue(updated);
    }
    @AfterEach
    void tearDown() {
        userService.deleteUser("test@com");
    }
}
