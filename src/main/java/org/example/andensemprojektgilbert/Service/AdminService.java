package org.example.andensemprojektgilbert.Service;

import org.example.andensemprojektgilbert.Infrastructure.UserRepo;
import org.example.andensemprojektgilbert.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @Autowired
    private UserRepo userRepo;

    public List<User> getUsers() {
        return userRepo.getAllUsers();
    }
}
