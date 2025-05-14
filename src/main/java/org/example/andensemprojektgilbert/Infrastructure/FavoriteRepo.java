package org.example.andensemprojektgilbert.Infrastructure;

import org.example.andensemprojektgilbert.Model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FavoriteRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Product> getFavorites(int userid) {
        String sql = "SELECT * FROM product JOIN favorite ON productid = id where userid = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Product(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("brand"),
                rs.getString("location"),
                rs.getString("description"),
                rs.getString("department"),
                rs.getString("category"),
                rs.getString("subcategory"),
                rs.getDate("posted_date"),
                rs.getDouble("price"),
                rs.getString("p_condition"),
                rs.getString("size"),
                rs.getString("color"),
                rs.getString("imgsrc"),
                rs.getInt("createdByID")
                ), userid);
    }

    public boolean addFavorite(int userId, int productId) {
        String sql = "INSERT INTO favorite (userid, productid) VALUES (?, ?)";
        try {
            int ok = jdbcTemplate.update(sql, userId, productId);
            if (ok == 1) {
                return true;
            }
        } catch (DataIntegrityViolationException e) {
                return false;
            }
        return false;
    }
    public boolean removeAsFavorite(int userId, int productId) {
        String sql = "DELETE FROM gilbert.favorite WHERE userid = ? AND productid = ?;";
        try {
            int ok = jdbcTemplate.update(sql, userId, productId);
            if (ok == 1) {
                return true;
            }
        } catch (DataIntegrityViolationException e) {
            return false;
        }
        return false;
    }
}
