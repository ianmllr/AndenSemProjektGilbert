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

    private Integer getColorId(String color) {
        String sql = "SELECT id from Color WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, color);
    }

    private Integer getSizeId(String size) {
        String sql = "SELECT id FROM Size WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{size}, Integer.class);
    }

    private Integer getConditionId(String condition) {
        String sql = "SELECT id FROM ItemCondition WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{condition}, Integer.class);
    }

    private Integer getBrandId(String brandName) {
        String sql = "SELECT id FROM Brand WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, brandName);
    }
    private Integer getCategoryId(String categoryName) {
        String sql = "SELECT id FROM Category WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, categoryName);
    }
    private Integer getSubcategoryId(String subcategoryName) {
        String sql = "SELECT id FROM Subcategory WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, subcategoryName);
    }
    private Integer getLocationId(String locationName) {
        String sql = "SELECT id FROM Location WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, locationName);
    }
    private Integer getDepartmentId(String departmentName) {
        String sql = "SELECT id FROM Department WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, departmentName);
    }

    // CREATE
    public void createProduct(Product product, User user) {
        Integer brandId = getBrandId(product.getBrand());
        Integer categoryId = getCategoryId(product.getCategory());
        Integer subcategoryId = getSubcategoryId(product.getSubcategory());
        Integer locationId = getLocationId(product.getLocation());
        Integer departmentId = getDepartmentId(product.getDepartment());

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
                product.getCondition(),
                product.getSize(),
                product.getColor(),
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
                rs.getString("itemcondition"),
                rs.getString("size"),
                rs.getString("color"),
                rs.getString("imgsrc"),
                rs.getInt("createdByID")
        ));
    }

    public List<Product> readAllProducts() {
        String sql = "SELECT p.id, p.name, b.name AS brand, l.name AS location, p.description,\n" +
                "d.name AS department, c.name AS category, s.name AS subcategory,\n" +
                "p.posted_date, p.price, i.itemcondition AS `itemcondition`, p.size, o.color AS color,\n" +
                "p.imgsrc, u.name AS createdBy\n" +
                "FROM Product p\n" +
                "LEFT JOIN Brand b ON p.brand_id = b.id\n" +
                "LEFT JOIN Location l ON p.location_id = l.id\n" +
                "LEFT JOIN Category_Department cd ON p.department_id = cd.department_id\n" +
                "LEFT JOIN Department d ON cd.department_id = d.id\n" +
                "LEFT JOIN Category c ON cd.category_id = c.id\n" +
                "LEFT JOIN Subcategory s ON p.subcategory_id = s.id\n" +
                "LEFT JOIN gilbert.p_condition i ON i.p_condition = p.condition_id\n" +
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
                rs.getString("p_condition"),
                rs.getString("size"),
                rs.getString("color"),
                rs.getString("imgsrc"),
                rs.getInt("createdByID")
        ));
    }

    public Product readProduct(int id) {
        String sql = "SELECT p.id, p.name, b.name AS brand, l.name AS location, p.description, " +
                "d.name AS department, c.name AS category, s.name AS subcategory, " +
                "p.posted_date, p.price, p.p_condition, p.size, p.color, " +
                "p.imgsrc, p.createdBy " +
                "FROM product p " +
                "JOIN brand b ON p.brand_id = b.id " +
                "JOIN location l ON p.location_id = l.id " +
                "JOIN department d ON p.department_id = d.id " +
                "JOIN category c ON p.category_id = c.id " +
                "JOIN subcategory s ON p.subcategory_id = s.id " +
                "WHERE p.id = ?";

        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> new Product(
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

    public void updateProduct(Product product) {
        String sql = "UPDATE Product SET name = ?, brand_id = ?, location_id = ?, description = ?, " +
                "department_id = ?, category_id = ?, subcategory_id = ?, posted_date = ?, price = ?, " +
                "condition_id = ?, size = ?, color_id = ?, imgsrc = ? WHERE id = ?";
        int brandId = getBrandId(product.getBrand());
        int categoryId = getCategoryId(product.getCategory());
        int subcategoryId = getSubcategoryId(product.getSubcategory());
        int colorId = getColorId(product.getColor());
        int conditionId = getConditionId(product.getCondition());
        int departmentId = getDepartmentId(product.getDepartment());
        int locationId = getLocationId(product.getLocation());
        int sizeId = getSizeId(product.getSize());

        jdbcTemplate.update(sql, product.getName(), brandId, locationId, product.getDescription(),
                departmentId, categoryId, subcategoryId, product.getPostedDate(), product.getPrice(),
                conditionId, sizeId, colorId, product.getImgsrc(), product.getId());
    }




}
