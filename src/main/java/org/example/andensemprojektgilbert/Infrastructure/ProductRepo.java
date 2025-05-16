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

    public Integer getBrandId(String brandName) {
        String sql = "SELECT id FROM Brand WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, brandName);
    }
    public Integer getCategoryId(String categoryName) {
        String sql = "SELECT id FROM Category WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, categoryName);
    }
    public Integer getSubcategoryId(String subcategoryName) {
        String sql = "SELECT id FROM Subcategory WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, subcategoryName);
    }
    public Integer getLocationId(String locationName) {
        String sql = "SELECT id FROM Location WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, locationName);
    }
    public Integer getDepartmentId(String departmentName) {
        String sql = "SELECT id FROM Department WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, departmentName);
    }
    public Integer getConditionId(String conditionName) {
        String sql = "select idcondition from gilbert.condition where itemcondition = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, conditionName);
    }
    public Integer getColorId(String colorName) {
        String sql = "SELECT idcolor FROM Color WHERE color = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, colorName);
    }

    // CREATE
    public void createProduct(Product product, User user) {
        Integer brandId = getBrandId(product.getBrand());
        Integer categoryId = getCategoryId(product.getCategory());
        Integer subcategoryId = getSubcategoryId(product.getSubcategory());
        Integer locationId = getLocationId(product.getLocation());
        Integer departmentId = getDepartmentId(product.getDepartment());
        Integer conditionId = getConditionId(product.getCondition());
        Integer colorId = getColorId(product.getColor());


        String sql = "INSERT INTO Product (name, brand_id, location_id, description, department_id, category_id, subcategory_id, posted_date, price, condition_id, size, color_id, imgsrc, createdByID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                product.getName(),
                brandId,
                locationId,
                product.getDescription(),
                departmentId,
                categoryId,
                subcategoryId,
                product.getPostedDate(),
                product.getPrice(),
                conditionId,
                product.getSize(),
                colorId,
                product.getImgsrc(),
                user.getId()
        );
    }




    // READ
    public List<Product> getProducts(String sql) {

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
                rs.getString("createdBy")
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
                rs.getString("itemcondition"),
                rs.getString("size"),
                rs.getString("color"),
                rs.getString("imgsrc"),
                rs.getString("createdByID")
        ));
    }

    public List<Product> readAllProducts() {
        String sql = "SELECT p.id, p.name, b.name AS brand, l.name AS location, p.description,\n" +
                "d.name AS department, c.name AS category, s.name AS subcategory,\n" +
                "p.posted_date, p.price, i.itemcondition AS `condition`, p.size, o.color AS color,\n" +
                "p.imgsrc, u.name AS createdBy\n" +
                "FROM Product p\n" +
                "LEFT JOIN Brand b ON p.brand_id = b.id\n" +
                "LEFT JOIN Location l ON p.location_id = l.id\n" +
                "LEFT JOIN Category_Department cd ON p.department_id = cd.department_id\n" +
                "LEFT JOIN Department d ON cd.department_id = d.id\n" +
                "LEFT JOIN Category c ON cd.category_id = c.id\n" +
                "LEFT JOIN Subcategory s ON p.subcategory_id = s.id\n" +
                "LEFT JOIN gilbert.condition i ON i.idcondition = p.condition_id\n" +
                "LEFT JOIN gilbert.color o ON o.idcolor = p.color_id\n" +
                "LEFT JOIN `User` u ON p.createdByID = u.id\n;";
        return getProducts(sql);
    }

    // læser herre-produkter
    public List<Product> readMensProducts() {
        String sql = "SELECT p.id, p.name, b.name AS brand, l.name AS location, p.description,\n" +
                "       d.name AS department, c.name AS category, s.name AS subcategory,\n" +
                "       p.posted_date, p.price, i.itemcondition AS `condition`, p.size, o.color AS color,\n" +
                "       p.imgsrc, u.name AS createdBy\n" +
                "FROM Product p\n" +
                "JOIN Department d ON p.department_id = d.id\n" +
                "LEFT JOIN Brand b ON p.brand_id = b.id\n" +
                "LEFT JOIN Location l ON p.location_id = l.id\n" +
                "LEFT JOIN Category_Department cd ON cd.category_id = p.category_id\n" +
                "LEFT JOIN Category c ON cd.category_id = c.id\n" +
                "LEFT JOIN Subcategory s ON p.subcategory_id = s.id\n" +
                "LEFT JOIN gilbert.condition i ON i.idcondition = p.condition_id\n" +
                "LEFT JOIN gilbert.color o ON o.idcolor = p.color_id\n" +
                "LEFT JOIN `User` u ON p.createdByID = u.id\n" +
                "WHERE d.name = 'Men';";
        return getProducts(sql);
    }

    // læser kvinde-produkter
    public List<Product> readWomensProducts() {
        String sql = "SELECT p.id, p.name, b.name AS brand, l.name AS location, p.description,\n" +
                "       d.name AS department, c.name AS category, s.name AS subcategory,\n" +
                "       p.posted_date, p.price, i.itemcondition AS `condition`, p.size, o.color AS color,\n" +
                "       p.imgsrc, u.name AS createdBy\n" +
                "FROM Product p\n" +
                "JOIN Department d ON p.department_id = d.id\n" +
                "LEFT JOIN Brand b ON p.brand_id = b.id\n" +
                "LEFT JOIN Location l ON p.location_id = l.id\n" +
                "LEFT JOIN Category_Department cd ON cd.category_id = p.category_id\n" +
                "LEFT JOIN Category c ON cd.category_id = c.id\n" +
                "LEFT JOIN Subcategory s ON p.subcategory_id = s.id\n" +
                "LEFT JOIN gilbert.condition i ON i.idcondition = p.condition_id\n" +
                "LEFT JOIN gilbert.color o ON o.idcolor = p.color_id\n" +
                "LEFT JOIN `User` u ON p.createdByID = u.id\n" +
                "WHERE d.name = 'Women';";
        return getProducts(sql);
    }

    public List<Product> readRandomMensProducts() {
        String sql = "SELECT p.id, p.name, b.name AS brand, l.name AS location, p.description,\n" +
                "       d.name AS department, c.name AS category, s.name AS subcategory,\n" +
                "       p.posted_date, p.price, i.itemcondition AS `condition`, p.size, o.color AS color,\n" +
                "       p.imgsrc, u.name AS createdBy\n" +
                "FROM Product p\n" +
                "JOIN Department d ON p.department_id = d.id\n" +
                "LEFT JOIN Brand b ON p.brand_id = b.id\n" +
                "LEFT JOIN Location l ON p.location_id = l.id\n" +
                "LEFT JOIN Category_Department cd ON cd.category_id = p.category_id\n" +
                "LEFT JOIN Category c ON cd.category_id = c.id\n" +
                "LEFT JOIN Subcategory s ON p.subcategory_id = s.id\n" +
                "LEFT JOIN gilbert.condition i ON i.idcondition = p.condition_id\n" +
                "LEFT JOIN gilbert.color o ON o.idcolor = p.color_id\n" +
                "LEFT JOIN `User` u ON p.createdByID = u.id\n" +
                "WHERE d.name = 'Men'\n" +
                "ORDER BY RAND()\n" +
                "LIMIT 10;";
        return getProducts(sql);
    }

    public List<Product> readRandomWomensProducts() {
        String sql = "SELECT p.id, p.name, b.name AS brand, l.name AS location, p.description,\n" +
                "       d.name AS department, c.name AS category, s.name AS subcategory,\n" +
                "       p.posted_date, p.price, i.itemcondition AS `condition`, p.size, o.color AS color,\n" +
                "       p.imgsrc, u.name AS createdBy\n" +
                "FROM Product p\n" +
                "JOIN Department d ON p.department_id = d.id\n" +
                "LEFT JOIN Brand b ON p.brand_id = b.id\n" +
                "LEFT JOIN Location l ON p.location_id = l.id\n" +
                "LEFT JOIN Category_Department cd ON cd.category_id = p.category_id\n" +
                "LEFT JOIN Category c ON cd.category_id = c.id\n" +
                "LEFT JOIN Subcategory s ON p.subcategory_id = s.id\n" +
                "LEFT JOIN gilbert.condition i ON i.idcondition = p.condition_id\n" +
                "LEFT JOIN gilbert.color o ON o.idcolor = p.color_id\n" +
                "LEFT JOIN `User` u ON p.createdByID = u.id\n" +
                "WHERE d.name = 'Women'\n" +
                "ORDER BY RAND()\n" +
                "LIMIT 10;";
        return getProducts(sql);
    }

    public List<Product> readRandomBags() {
        String sql = "SELECT p.id, p.name, b.name AS brand, l.name AS location, p.description,\n" +
                "       d.name AS department, c.name AS category, s.name AS subcategory,\n" +
                "       p.posted_date, p.price, i.itemcondition AS `condition`, p.size, o.color AS color,\n" +
                "       p.imgsrc, u.name AS createdBy\n" +
                "FROM Product p\n" +
                "JOIN Department d ON p.department_id = d.id\n" +
                "LEFT JOIN Brand b ON p.brand_id = b.id\n" +
                "LEFT JOIN Location l ON p.location_id = l.id\n" +
                "LEFT JOIN Category_Department cd ON cd.category_id = p.category_id\n" +
                "LEFT JOIN Category c ON cd.category_id = c.id\n" +
                "LEFT JOIN Subcategory s ON p.subcategory_id = s.id\n" +
                "LEFT JOIN gilbert.condition i ON i.idcondition = p.condition_id\n" +
                "LEFT JOIN gilbert.color o ON o.idcolor = p.color_id\n" +
                "LEFT JOIN `User` u ON p.createdByID = u.id\n" +
                "WHERE d.name = 'Bags'\n" +
                "ORDER BY RAND()\n" +
                "LIMIT 10;";
        return getProducts(sql);
    }




    // andre produkttyper her


    public List<Product> readUserProducts(User user) {
        String sql = "SELECT p.id, p.name, b.name AS brand, l.name AS location, p.description,\n" +
                "       d.name AS department, c.name AS category, s.name AS subcategory,\n" +
                "       p.posted_date, p.price, i.itemcondition AS `condition`, p.size, o.color AS color,\n" +
                "       p.imgsrc, u.name AS createdBy\n" +
                "FROM Product p\n" +
                "JOIN `User` u ON p.createdByID = u.id\n" +
                "LEFT JOIN Brand b ON p.brand_id = b.id\n" +
                "LEFT JOIN Location l ON p.location_id = l.id\n" +
                "LEFT JOIN Category_Department cd ON cd.category_id = p.category_id\n" +
                "LEFT JOIN Department d ON cd.department_id = d.id\n" +
                "LEFT JOIN Category c ON cd.category_id = c.id\n" +
                "LEFT JOIN Subcategory s ON p.subcategory_id = s.id\n" +
                "LEFT JOIN gilbert.condition i ON i.idcondition = p.condition_id\n" +
                "LEFT JOIN gilbert.color o ON o.idcolor = p.color_id\n" +
                "WHERE u.id = ?;";
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
                rs.getString("condition"),
                rs.getString("size"),
                rs.getString("color"),
                rs.getString("imgsrc"),
                rs.getString("createdBy")
        ));
    }
}
