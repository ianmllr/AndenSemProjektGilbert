package org.example.andensemprojektgilbert.Infrastructure;

import org.example.andensemprojektgilbert.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public boolean createUser(User user) {
        String sql = "INSERT INTO user (name, password, email, sales, rating, role)";
        int result = jdbcTemplate.update(sql, user.getName(), user.getPassword(), user.getEmail(), user.getSales(), user.getRating(), user.getRole());
        return result == 1;
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
                return user;
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
