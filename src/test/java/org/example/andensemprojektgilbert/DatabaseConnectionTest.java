package org.example.andensemprojektgilbert;

import org.example.andensemprojektgilbert.Infrastructure.UserRepo;
import org.example.andensemprojektgilbert.Model.User;
import org.example.andensemprojektgilbert.Service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DatabaseConnectionTest {

    @Autowired
    private UserRepo userRepo;


    @Test
    void testConnection() throws SQLException {
        UserRepo userRepo = this.userRepo;
        User user = userRepo.readUserByEmail("Thomas");
        assertNull(user, "User should be null if not found");
    }
}
