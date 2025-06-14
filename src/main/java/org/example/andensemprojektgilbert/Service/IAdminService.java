package org.example.andensemprojektgilbert.Service;

import org.example.andensemprojektgilbert.Model.Condition;
import org.example.andensemprojektgilbert.Model.Location;
import org.example.andensemprojektgilbert.Model.Product;
import org.example.andensemprojektgilbert.Model.User;

import java.util.List;

public interface IAdminService {
    List<User> getUsers();
    boolean giveAdminRights(int userId);
    boolean removeAdminRights(int userId);
    List<User> searchForUser(String query);
    boolean deleteUser(int id);
    List<User> getUsersPage(int page, int size);
    List<Product> getProductsPage(int page, int size);
    boolean deleteProduct(int id);
    List<Product> getFilteredProducts(String query);
    User getUserById(int id);
    boolean createCondition(Condition condition);
    boolean createLocation(Location location);
}
