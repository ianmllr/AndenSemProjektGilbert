package org.example.andensemprojektgilbert.Service;

import org.example.andensemprojektgilbert.Model.User;
import org.springframework.security.crypto.bcrypt.BCrypt;

public interface IUserService {
    boolean register(User user);

    User login(String email, String password);

    boolean updateUser(User user);

    boolean deleteUser(String email);
}
