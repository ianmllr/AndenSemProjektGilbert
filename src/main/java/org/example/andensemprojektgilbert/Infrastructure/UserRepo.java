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
        int result = jdbcTemplate.update(sql, user.getName(), user.getPassword(), user.getEmail(), 0, user.getRating(),  2, user.getImgsrc());
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
        String sql = "SELECT * FROM user WHERE email = ?";

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

}
