package org.example.andensemprojektgilbert.Infrastructure;

import org.example.andensemprojektgilbert.Model.User;
import java.util.List;
import java.util.Optional;

public interface IUserRepo {
    boolean createUser(User user);

    List<User> getAllUsers();

    User readUserByEmail(String email);

    boolean updateUser(User user);
    boolean updateUserNoPassword(User user);
    boolean deleteUser(String email);
    boolean giveAdminRights(int id);
    boolean removeAdminRights(int id);
    boolean deleteUserById(int id);
    List<User> getUserPages(int page, int size);
    Optional<User> getUserById(int id);
}
