package org.example.andensemprojektgilbert.Service;

import org.example.andensemprojektgilbert.Infrastructure.ProductRepo;
import org.example.andensemprojektgilbert.Infrastructure.UserRepo;
import org.example.andensemprojektgilbert.Model.Condition;
import org.example.andensemprojektgilbert.Model.Location;
import org.example.andensemprojektgilbert.Model.Product;
import org.example.andensemprojektgilbert.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImp implements IAdminService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProductRepo productRepo;

    @Override
    public List<User> getUsers() {
        return userRepo.getAllUsers();
    }

    @Override
    public boolean giveAdminRights(int id) {
        return userRepo.giveAdminRights(id);
    }
    @Override
    public boolean removeAdminRights(int id) {
        return userRepo.removeAdminRights(id);
    }
    @Override
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
    @Override
    public boolean deleteUser(int id) {
        return userRepo.deleteUserById(id);
    }
    @Override
    public List<User> getUsersPage(int page, int size) {
        return userRepo.getUserPages(page, size);
    }
    @Override
    public List<Product> getProductsPage(int page, int size) {
        return productRepo.getProductsPage(page, size);
    }
    @Override
    public boolean deleteProduct(int id) {
        return productRepo.deleteProductById(id);
    }
    @Override
    public List<Product> getFilteredProducts(String query) {
        List<Product> filteredProducts = productRepo.searchProducts(query);
        return filteredProducts;
    }
    @Override
    public User getUserById(int id) {
        Optional<User> user = userRepo.getUserById(id);
        if (user.isPresent()) {
            return user.get();
        }

        return null;
    }
    @Override
    public boolean createCondition(Condition condition) {
       return productRepo.createCondition(condition);
    }
    @Override
    public boolean createLocation(Location location) {
        return productRepo.createLocation(location);
    }
}
