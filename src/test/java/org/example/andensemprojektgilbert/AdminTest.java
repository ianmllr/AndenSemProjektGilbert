package org.example.andensemprojektgilbert;

import org.example.andensemprojektgilbert.Model.User;
import org.example.andensemprojektgilbert.Service.AdminService;
import org.example.andensemprojektgilbert.Service.FavoriteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AdminTest {

    @Autowired
    private AdminService adminService;

    @BeforeEach
    void setUp() {
        User testUser = adminService.getUser(1);
        if (testUser != null && testUser.getRole().equals("admin")) {
            adminService.removeAdminRights(testUser.getId());
        }
    }

    @Test
    void testGiveAdminRole() {
        User testUser = adminService.getUser(1);
        adminService.giveAdminRights(testUser.getId());
        testUser = adminService.getUser(1);
        assertEquals("admin", testUser.getRole());
    }
    @Test
    void testRemoveAdminRights() {
        User testUser = adminService.getUser(1);
        //da setup fjerner adminrettigheder, tjekker vi blot om de er user
        assertEquals("user", testUser.getRole());
    }
}
