package org.example.andensemprojektgilbert.Service;

import org.example.andensemprojektgilbert.Infrastructure.UserRepo;
import org.example.andensemprojektgilbert.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    private User user;


    public boolean register(User user) {
        if (userRepo.readUserByEmail(user.getEmail()) != null) {
            return false;
        }
        String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashed);
        return userRepo.createUser(user);
    }

    public User login(String email, String password) {
        user = userRepo.readUserByEmail(email);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            System.out.println("Bruger logget ind: " + user.getName());
            return user;
        } else {
            return null;
        }
    }
    public boolean updateUser(User user) {
        if (user != null && user.getPassword().isEmpty()) {
            return userRepo.updateUserNoPassword(user);
        }
        if (user != null && !user.getPassword().isEmpty()) {
            String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            user.setPassword(hashed);
            return userRepo.updateUser(user);
        }
        else return false;
    }
    public boolean deleteUser(String email) {
        return userRepo.deleteUser(email);
    }
}
