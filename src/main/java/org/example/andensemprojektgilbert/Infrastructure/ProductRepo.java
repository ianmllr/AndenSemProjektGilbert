package org.example.andensemprojektgilbert.Infrastructure;

import org.example.andensemprojektgilbert.Model.Product;
import org.example.andensemprojektgilbert.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    // CREATE
    public void createProduct(Product product) {
        String sql = "INSERT INTO product (name, brand, location, description, department, category, subcategory, posted_date, price, p_condition, size, color, imgsrc, createdByID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                product.getName(),
                product.getBrand(),
                product.getLocation(),
                product.getDescription(),
                product.getDepartment(),
                product.getCategory(),
                product.getSubcategory(),
                product.getPostedDate(),
                product.getPrice(),
                product.getP_condition(),
                product.getSize(),
                product.getColor(),
                product.getImgsrc(),
                product.getCreatedByID()
        );
    }

    // READ
    private List<Product> getProducts(String sql) {
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
        ));
    }

    public List<Product> searchProducts(String keyword) {
        // Laver en sql forspørgelse hvor den bare tjekker efter brand, description, department, category, subcategory eller name, så hvis man søger på noget af det
        // Finder den produkter efter det
        String sql = "SELECT * FROM Product WHERE brand LIKE ? OR description LIKE ? OR department LIKE ? OR category LIKE ? OR subcategory LIKE ? OR name LIKE ?";
        // Det bliver brugt for at lave sql forsårøgelse skal man have % ord % replace sørger bare for at man kan søge ord med tegnet
        String searchTerm = "%" + keyword + "%";

        // Indsætter searchTerm for hver ting mna søger efter, så hvis jeg søger Adidas fx, så sætter den det ind i hver searchTerm.
        return jdbcTemplate.query(sql, new Object[]{searchTerm, searchTerm, searchTerm, searchTerm, searchTerm, searchTerm}, (rs, rowNum) -> new Product(
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
        ));
    }

    public List<Product> readAllProducts() {
        String sql = "SELECT * FROM Product";
        return getProducts(sql);
    }

    // læser herre-produkter
    public List<Product> readMensProducts() {
        String sql = "SELECT * FROM Product WHERE department = 'Men'";
        return getProducts(sql);
    }

    // læser kvinde-produkter
    public List<Product> readWomensProducts() {
        String sql = "SELECT * FROM Product WHERE department = 'Women'";
        return getProducts(sql);
    }

    public List<Product> readRandomMensProducts() {
        String sql = "SELECT * FROM Product WHERE department = 'Men' ORDER BY RAND() LIMIT 10";
        return getProducts(sql);
    }

    public List<Product> readRandomWomensProducts() {
        String sql = "SELECT * FROM Product WHERE department = 'Women' ORDER BY RAND() LIMIT 10";
        return getProducts(sql);
    }

    public List<Product> readRandomBags() {
        String sql = "SELECT * FROM Product WHERE category = 'Bags' ORDER BY RAND() LIMIT 10";
        return getProducts(sql);
    }




    // andre produkttyper her


    public List<Product> readUserProducts(User user) {


        String sql = "SELECT * FROM Product WHERE createdByID = ?";
        return jdbcTemplate.query(sql, new Object[]{user.getId()}, (rs, rowNum) -> new Product(
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
        ));
    }


}
