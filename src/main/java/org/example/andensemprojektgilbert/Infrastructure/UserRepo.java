package org.example.andensemprojektgilbert.Infrastructure;

import org.example.andensemprojektgilbert.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
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
}
