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

    public List<Product> getFavorites(int userId) {
        String sql = "SELECT p.id, p.name, b.name AS brand, l.name AS location, p.description, \n" +
                "d.name AS department, c.name AS category, s.name AS subcategory,\n" +
                "p.posted_date, p.price, i.itemcondition AS `condition`, p.size, o.color AS color,\n" +
                "p.imgsrc, u.name AS createdBy\n" +
                "FROM gilbert.Favorite f\n" +
                "JOIN gilbert.Product p ON f.productid = p.id\n" +
                "LEFT JOIN gilbert.Brand b ON p.brand_id = b.id\n" +
                "LEFT JOIN gilbert.Location l ON p.location_id = l.id\n" +
                "LEFT JOIN gilbert.Category_Department cd ON cd.department_id = p.department_id\n" +
                "LEFT JOIN gilbert.Department d ON cd.department_id = d.id \n" +
                "LEFT JOIN gilbert.Category c ON cd.category_id = c.id\n" +
                "LEFT JOIN gilbert.condition i ON i.idcondition = p.condition_id\n" +
                "LEFT JOIN gilbert.color o ON o.idcolor = p.color_id\n" +
                "LEFT JOIN gilbert.Subcategory s ON p.subcategory_id = s.id\n" +
                "LEFT JOIN gilbert.`User` u ON p.createdByID = u.id\n" +
                "WHERE f.userid = ?;";

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
                rs.getString("condition"),
                rs.getString("size"),
                rs.getString("color"),
                rs.getString("imgsrc"),
                rs.getInt("createdByID") // Changed to display_name from Users table
        ), userId);
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
