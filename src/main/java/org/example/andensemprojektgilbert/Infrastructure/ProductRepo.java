package org.example.andensemprojektgilbert.Infrastructure;

import org.example.andensemprojektgilbert.Model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Product> searchProducts(String keyword) {
        String sql = "SELECT * FROM Product WHERE brand LIKE ? OR description LIKE ? OR department LIKE ? OR category LIKE ? OR name LIKE ?";
        String searchTerm = "%" + keyword + "%";

        return jdbcTemplate.query(sql, new Object[]{searchTerm, searchTerm, searchTerm, searchTerm, searchTerm},
                (rs, rowNum) -> new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("brand"),
                        rs.getString("location"),
                        rs.getString("description"),
                        rs.getString("department"),
                        rs.getString("category"),
                        rs.getDate("posted_date"),
                        rs.getDouble("price"),
                        rs.getString("p_condition"),
                        rs.getString("size"),
                        rs.getString("color")
                )
        );
    }
}
