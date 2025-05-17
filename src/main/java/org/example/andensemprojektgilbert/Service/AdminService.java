package org.example.andensemprojektgilbert.Service;

import org.example.andensemprojektgilbert.Infrastructure.UserRepo;
import org.example.andensemprojektgilbert.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {
    @Autowired
    private UserRepo userRepo;

    public List<User> getUsers() {
        return userRepo.getAllUsers();
    }
    public User getUser(int id) {
        List<User> users = getUsers();
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }
    public boolean giveAdminRights(int id) {
        return userRepo.giveAdminRights(id);
    }
    public boolean removeAdminRights(int id) {
        return userRepo.removeAdminRights(id);
    }
    public List<User> searchForUser(String query) {
        List<User> users = getUsers();
        List<User> filteredUsers = new ArrayList<>();
        for (User user : users) {
            if (user.getName().toLowerCase().contains(query.toLowerCase()) || user.getEmail().toLowerCase().contains(query.toLowerCase())) {
                filteredUsers.add(user);
            }
        }
        return filteredUsers;
    }
    public boolean deleteUser(int id) {
        return userRepo.deleteUserById(id);
    }
    public List<User> getUsersPage(int page, int size) {
        return userRepo.getUserPages(page, size);
    }
}
