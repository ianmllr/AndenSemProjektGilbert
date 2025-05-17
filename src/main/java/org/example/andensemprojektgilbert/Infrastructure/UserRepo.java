package org.example.andensemprojektgilbert.Infrastructure;

import org.example.andensemprojektgilbert.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public boolean createUser(User user) {
        String sql = "INSERT INTO user (name, password, email, sales, rating, role, imgsrc) VALUES (?, ?, ?, ?, ?, ?, ?)";
        int result = jdbcTemplate.update(sql, user.getName(), user.getPassword(), user.getEmail(), 0, user.getRating(), 2, user.getImgsrc());
        return result == 1;
    }

    public List<User> getAllUsers() {
        String sql = "SELECT id, name, email, password, sales, rating, user_role.role, imgsrc FROM user\n"  +
                "JOIN user_role ON idroles = user.role";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new User(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("password"), rs.getInt("sales"), rs.getString("rating"), rs.getString("role"), rs.getString("imgsrc"))
        );
    }

    public User readUserByEmail(String email) {
        String sql = "SELECT u.id, u.name, u.password, u.email, u.sales, u.rating, u.imgsrc, ur.role AS role\n" +
                "FROM gilbert.user u\n" +
                "JOIN user_role ur ON ur.idroles = u.role\n" +
                "WHERE u.email = ?;" ;

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{email}, (rs, rowNum) -> {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setSales(rs.getInt("sales"));
                user.setRating(rs.getString("rating"));
                user.setRole(rs.getString("role"));
                user.setImgsrc(rs.getString("imgsrc"));
                return user;
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    public boolean updateUser(User user) {
        String sql = "UPDATE user SET name = ?, password = ?, email = ?, imgsrc = ? WHERE id = ?";
        int updated = jdbcTemplate.update(sql, user.getName(), user.getPassword(), user.getEmail(), user.getImgsrc(), user.getId());
        if (updated > 0) {
            return true;
        }
        return false;
    }
    public boolean updateUserNoPassword(User user) {
        String sql = "UPDATE user set name = ?, email = ?, imgsrc = ? WHERE id = ?";
        int updated = jdbcTemplate.update(sql, user.getName(), user.getEmail(), user.getImgsrc(), user.getId());
        if (updated > 0) {
            return true;
        }
        return false;
    }
    public boolean deleteUser(String email) {
        String sql = "DELETE FROM user WHERE email = ?";
        int result = jdbcTemplate.update(sql, email);
        if (result > 0) {
            return true;
        }
        return false;
    }
    public boolean giveAdminRights(int id) {
        String sql = "UPDATE gilbert.user set role = ? where id = ?";
        int updated = jdbcTemplate.update(sql, 1, id);
        if (updated > 0) {
            return true;
        }
        return false;
    }
    public boolean removeAdminRights(int id) {
        String sql = "UPDATE gilbert.user SET role = ? WHERE id = ?";
        int updated = jdbcTemplate.update(sql, 2, id);
        if (updated > 0) {
            return true;
        }
        return false;
    }
    public boolean deleteUserById(int id) {
        String sql = "delete from gilbert.user where id = ?";
        int deleted = jdbcTemplate.update(sql, id);
        if (deleted > 0) {
            return true;
        }
        return false;
    }
    public List<User> getUserPages(int page, int size) {
        String sql = "SELECT u.id, u.name, u.email, u.password, u.sales, u.rating, r.role, u.imgsrc\n" +
                "FROM gilbert.user u\n" +
                "JOIN user_role r ON u.role = r.idroles\n" +
                "LIMIT ? OFFSET ?;";
        int offset = (page - 1) * size;
        return jdbcTemplate.query(sql, new Object[]{size, offset}, (rs, rowNum) ->
                new User(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("password"), rs.getInt("sales"), rs.getString("rating"), rs.getString("role"), rs.getString("imgsrc")));
        }
}
