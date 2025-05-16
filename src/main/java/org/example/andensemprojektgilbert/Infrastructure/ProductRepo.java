package org.example.andensemprojektgilbert.Infrastructure;

import org.example.andensemprojektgilbert.Model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Integer getColorId(String color) {
        String sql = "SELECT idcolor from Color WHERE color = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, color);
    }

    private Integer getSizeId(String sizeValue) {
        String sql = "SELECT id FROM size WHERE size_value = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{sizeValue}, Integer.class);
    }

    private Integer getConditionId(String condition) {
        String sql = "SELECT idcondition FROM gilbert.condition WHERE itemcondition = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{condition}, Integer.class);
    }

    private Integer getBrandId(String brandName) {
        String sql = "SELECT id FROM Brand WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, brandName);
    }
    private Integer getCategoryId(String categoryName) {
        System.out.println("Looking for category: '" + categoryName + "'");
        String sql = "SELECT id FROM Category WHERE name = ?";
        try {
            Integer categoryId = jdbcTemplate.queryForObject(sql, Integer.class, categoryName);
            System.out.println("Found category ID: " + categoryId + " for category: " + categoryName);
            return categoryId;
        } catch (EmptyResultDataAccessException e) {
            System.out.println("No category found for: '" + categoryName + "'");
            return null;
        }
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
        Integer sizeId = getSizeId(product.getSize());
        Integer colorId = getColorId(product.getColor());
        Integer conditionId = getConditionId(product.getCondition());

        String sql = "INSERT INTO Product (name, brand_id, location_id, description, department_id, category_id, subcategory_id, posted_date, price, condition_id, size_id, color_id, imgsrc, createdByID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
                sizeId,
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
                rs.getString("item_condition"),
                rs.getString("size"),
                rs.getString("color"),
                rs.getString("imgsrc"),
                rs.getInt("createdByID")
        ));
    }


    public List<Product> searchProducts(String keyword) {
        String sql = "SELECT p.id, p.name, b.name AS brand, l.name AS location, p.description, " +
                "d.name AS department, c.name AS category, sc.name AS subcategory, " +
                "p.posted_date, p.price, cond.itemcondition AS `condition`, s.size_value AS size, col.color AS color, " +
                "p.imgsrc, p.createdbyid " +
                "FROM product p " +
                "LEFT JOIN brand b ON p.brand_id = b.id " +
                "LEFT JOIN location l ON p.location_id = l.id " +
                "LEFT JOIN department d ON p.department_id = d.id " +
                "LEFT JOIN category c ON p.category_id = c.id " +
                "LEFT JOIN category sc ON p.subcategory_id = sc.id " +
                "LEFT JOIN color col ON p.color_id = col.idcolor " +
                "LEFT JOIN `condition` cond ON p.condition_id = cond.idcondition " +
                "LEFT JOIN size s ON p.size_id = s.id " +
                "WHERE p.name LIKE ? OR b.name LIKE ? OR l.name LIKE ? OR d.name LIKE ? OR c.name LIKE ? OR sc.name LIKE ? OR p.description LIKE ?";
        String searchTerm = "%" + keyword + "%";
        return jdbcTemplate.query(sql, new Object[]{searchTerm, searchTerm, searchTerm, searchTerm, searchTerm, searchTerm, searchTerm}, (rs, rowNum) -> new Product(
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
                rs.getInt("createdByID")
        ));
    }


    public List<Product> readAllProducts() {
        String sql = "SELECT p.id, p.name, b.name AS brand, l.name AS location, p.description,\n" +
                "       d.name AS department, c.name AS category, sc.name AS subcategory,\n" +
                "       p.posted_date, p.price, cond.itemcondition AS item_condition,\n" +
                "       s.size_value AS size, col.color AS color,\n" +
                "       p.imgsrc, p.createdbyid\n" +
                "FROM product p\n" +
                "INNER JOIN department d ON p.department_id = d.id \n" +
                "INNER JOIN category c ON p.category_id = c.id\n" +
                "INNER JOIN subcategory sc ON p.subcategory_id = sc.id\n" +
                "LEFT JOIN brand b ON p.brand_id = b.id\n" +
                "LEFT JOIN location l ON p.location_id = l.id\n" +
                "LEFT JOIN color col ON p.color_id = col.idcolor\n" +
                "LEFT JOIN `condition` cond ON p.condition_id = cond.idcondition\n" +
                "LEFT JOIN size s ON p.size_id = s.id";
        return getProducts(sql);
    }

    // læser herre-produkter
    public List<Product> readMensProducts() {
        String sql = "SELECT p.id, p.name, b.name AS brand, l.name AS location, p.description,\n" +
                "       d.name AS department, c.name AS category, sc.name AS subcategory,\n" +
                "       p.posted_date, p.price, cond.itemcondition AS item_condition,\n" +
                "       s.size_value AS size, col.color AS color,\n" +
                "       p.imgsrc, p.createdbyid\n" +
                "FROM product p\n" +
                "INNER JOIN department d ON p.department_id = d.id \n" +
                "INNER JOIN category c ON p.category_id = c.id\n" +
                "INNER JOIN subcategory sc ON p.subcategory_id = sc.id\n" +
                "LEFT JOIN brand b ON p.brand_id = b.id\n" +
                "LEFT JOIN location l ON p.location_id = l.id\n" +
                "LEFT JOIN color col ON p.color_id = col.idcolor\n" +
                "LEFT JOIN `condition` cond ON p.condition_id = cond.idcondition\n" +
                "LEFT JOIN size s ON p.size_id = s.id\n" +
                "WHERE d.name = 'Men';";
        return getProducts(sql);
    }

    // læser kvinde-produkter
    public List<Product> readWomensProducts() {
        String sql = "SELECT p.id, p.name, b.name AS brand, l.name AS location, p.description,\n" +
                "       d.name AS department, c.name AS category, sc.name AS subcategory,\n" +
                "       p.posted_date, p.price, cond.itemcondition AS item_condition,\n" +
                "       s.size_value AS size, col.color AS color,\n" +
                "       p.imgsrc, p.createdbyid\n" +
                "FROM product p\n" +
                "INNER JOIN department d ON p.department_id = d.id \n" +
                "INNER JOIN category c ON p.category_id = c.id\n" +
                "INNER JOIN subcategory sc ON p.subcategory_id = sc.id\n" +
                "LEFT JOIN brand b ON p.brand_id = b.id\n" +
                "LEFT JOIN location l ON p.location_id = l.id\n" +
                "LEFT JOIN color col ON p.color_id = col.idcolor\n" +
                "LEFT JOIN `condition` cond ON p.condition_id = cond.idcondition\n" +
                "LEFT JOIN size s ON p.size_id = s.id\n" +
                "WHERE d.name = 'Women'";
        return getProducts(sql);
    }

    public List<Product> readRandomMensProducts() {
        String sql = "SELECT p.id, p.name, b.name AS brand, l.name AS location, p.description,\n" +
                "       d.name AS department, c.name AS category, sc.name AS subcategory,\n" +
                "       p.posted_date, p.price, cond.itemcondition AS item_condition,\n" +
                "       s.size_value AS size, col.color AS color,\n" +
                "       p.imgsrc, p.createdbyid\n" +
                "FROM product p\n" +
                "INNER JOIN department d ON p.department_id = d.id \n" +
                "INNER JOIN category c ON p.category_id = c.id\n" +
                "INNER JOIN subcategory sc ON p.subcategory_id = sc.id\n" +
                "LEFT JOIN brand b ON p.brand_id = b.id\n" +
                "LEFT JOIN location l ON p.location_id = l.id\n" +
                "LEFT JOIN color col ON p.color_id = col.idcolor\n" +
                "LEFT JOIN `condition` cond ON p.condition_id = cond.idcondition\n" +
                "LEFT JOIN size s ON p.size_id = s.id\n" +
                "WHERE d.name = 'Men' ORDER BY RAND() LIMIT 10";
        return getProducts(sql);
    }

    public List<Product> readRandomWomensProducts() {
        String sql = "SELECT p.id, p.name, b.name AS brand, l.name AS location, p.description,\n" +
                "       d.name AS department, c.name AS category, sc.name AS subcategory,\n" +
                "       p.posted_date, p.price, cond.itemcondition AS item_condition,\n" +
                "       s.size_value AS size, col.color AS color,\n" +
                "       p.imgsrc, p.createdbyid\n" +
                "FROM product p\n" +
                "INNER JOIN department d ON p.department_id = d.id \n" +
                "INNER JOIN category c ON p.category_id = c.id\n" +
                "INNER JOIN subcategory sc ON p.subcategory_id = sc.id\n" +
                "LEFT JOIN brand b ON p.brand_id = b.id\n" +
                "LEFT JOIN location l ON p.location_id = l.id\n" +
                "LEFT JOIN color col ON p.color_id = col.idcolor\n" +
                "LEFT JOIN `condition` cond ON p.condition_id = cond.idcondition\n" +
                "LEFT JOIN size s ON p.size_id = s.id\n" +
                "WHERE d.name = 'Women' ORDER BY RAND() LIMIT 10";
        return getProducts(sql);
    }

    public List<Product> readRandomBags() {
        String sql = "SELECT p.id, p.name, b.name AS brand, l.name AS location, p.description,\n" +
                "       d.name AS department, c.name AS category, sc.name AS subcategory,\n" +
                "       p.posted_date, p.price, cond.itemcondition AS item_condition,\n" +
                "       s.size_value AS size, col.color AS color,\n" +
                "       p.imgsrc, p.createdbyid\n" +
                "FROM product p\n" +
                "INNER JOIN department d ON p.department_id = d.id \n" +
                "INNER JOIN category c ON p.category_id = c.id\n" +
                "INNER JOIN subcategory sc ON p.subcategory_id = sc.id\n" +
                "LEFT JOIN brand b ON p.brand_id = b.id\n" +
                "LEFT JOIN location l ON p.location_id = l.id\n" +
                "LEFT JOIN color col ON p.color_id = col.idcolor\n" +
                "LEFT JOIN `condition` cond ON p.condition_id = cond.idcondition\n" +
                "LEFT JOIN size s ON p.size_id = s.id\n" +
                "WHERE d.name = 'Bags' ORDER BY RAND() LIMIT 10";
        return getProducts(sql);
    }

    public List<Product> readMensProductsBySubCat(String subcategory) {
        String sql = "SELECT p.id, p.name, b.name AS brand, l.name AS location, p.description, " +
                "d.name AS department, c.name AS category, sc.name AS subcategory, " +
                "p.posted_date, p.price, cond.itemcondition AS `condition`, s.size_value AS size, col.color AS color, " +
                "p.imgsrc, p.createdbyid " +
                "FROM product p " +
                "LEFT JOIN brand b ON p.brand_id = b.id " +
                "LEFT JOIN location l ON p.location_id = l.id " +
                "LEFT JOIN department d ON p.department_id = d.id " +
                "LEFT JOIN category c ON p.category_id = c.id " +
                "LEFT JOIN category sc ON p.subcategory_id = sc.id " +
                "LEFT JOIN color col ON p.color_id = col.idcolor " +
                "LEFT JOIN `condition` cond ON p.condition_id = cond.idcondition " +
                "LEFT JOIN size s ON p.size_id = s.id " +
                "WHERE d.name = 'Men' AND sc.name = ?";
        return jdbcTemplate.query(sql, new Object[]{subcategory}, (rs, rowNum) -> new Product(
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
                rs.getInt("createdByID")
        ));
    }






    // andre produkttyper her


    public List<Product> readUserProducts(User user) {
        String sql = "SELECT p.id, p.name, b.name AS brand, l.name AS location, p.description, " +
                "d.name AS department, c.name AS category, sc.name AS subcategory, " +
                "p.posted_date, p.price, cond.itemcondition AS `condition`, s.size_value AS size, col.color AS color, " +
                "p.imgsrc, p.createdbyid " +
                "FROM product p " +
                "LEFT JOIN brand b ON p.brand_id = b.id " +
                "LEFT JOIN location l ON p.location_id = l.id " +
                "LEFT JOIN department d ON p.department_id = d.id " +
                "LEFT JOIN category c ON p.category_id = c.id " +
                "LEFT JOIN category sc ON p.subcategory_id = sc.id " +
                "LEFT JOIN color col ON p.color_id = col.idcolor " +
                "LEFT JOIN `condition` cond ON p.condition_id = cond.idcondition " +
                "LEFT JOIN size s ON p.size_id = s.id " +
                "WHERE p.createdbyid = ?";
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
                rs.getInt("createdByID")
        ));
    }

    public Product readProduct(int id) {
        String sql = "SELECT p.id, p.name, b.name AS brand, l.name AS location, p.description, " +
                "d.name AS department, c.name AS category, sc.name AS subcategory, " +
                "p.posted_date, p.price, cond.itemcondition AS `condition`, s.size_value AS size, col.color AS color, " +
                "p.imgsrc, p.createdbyid " +
                "FROM product p " +
                "LEFT JOIN brand b ON p.brand_id = b.id " +
                "LEFT JOIN location l ON p.location_id = l.id " +
                "LEFT JOIN department d ON p.department_id = d.id " +
                "LEFT JOIN category c ON p.category_id = c.id " +
                "LEFT JOIN category sc ON p.subcategory_id = sc.id " +
                "LEFT JOIN color col ON p.color_id = col.idcolor " +
                "LEFT JOIN `condition` cond ON p.condition_id = cond.idcondition " +
                "LEFT JOIN size s ON p.size_id = s.id " +
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
                rs.getString("condition"),
                rs.getString("size"),
                rs.getString("color"),
                rs.getString("imgsrc"),
                rs.getInt("createdByID")
        ));
    }

    public void updateProduct(Product product) {
        String sql = "UPDATE product SET name = ?, brand_id = ?, location_id = ?, description = ?, " +
                "department_id = ?, category_id = ?, subcategory_id = ?, posted_date = ?, price = ?, " +
                "condition_id = ?, size_id = ?, color_id = ?, imgsrc = ? WHERE id = ?";
        int brandId = getBrandId(product.getBrand());
        int categoryId = getCategoryId(product.getCategory());
        int subcategoryId = getSubcategoryId(product.getSubcategory());
        int colorId = getColorId(product.getColor());
        int conditionId = getConditionId(product.getCondition());
        int departmentId = getDepartmentId(product.getDepartment());
        int locationId = getLocationId(product.getLocation());
        int sizeId = getSizeId(product.getSize()); // Henter size-id ud fra tekst

        jdbcTemplate.update(sql, product.getName(), brandId, locationId, product.getDescription(),
                departmentId, categoryId, subcategoryId, product.getPostedDate(), product.getPrice(),
                conditionId, sizeId, colorId, product.getImgsrc(), product.getId());
    }

    public List<String> getDepartments() {
        String sql = "SELECT name FROM Department";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    public List<Subcategory> getSubcategories() {
        String sql = "SELECT * FROM subcategory";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Subcategory sub = new Subcategory();
            sub.setName(rs.getString("name"));
            sub.setCategoryId(rs.getInt("category_id"));
            sub.setSizeTypeId(rs.getInt("size_type_id"));
            return sub;
        });
    }

    public List<Category> getCategories() {
        String sql = "SELECT * FROM Category";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Category cat = new Category();
            cat.setId(rs.getInt("id"));
            cat.setName(rs.getString("name"));
            return cat;
        });
    }


    public List<String> getBrands() {
        String sql ="SELECT name FROM Brand";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    public List<String> getLocations() {
        String sql = "SELECT name FROM Location";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    public List<String> getConditions() {
        String sql = "SELECT itemcondition FROM `condition`";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    public List<String> getColors() {
        String sql = "SELECT color FROM color";
        return jdbcTemplate.queryForList(sql, String.class);
    }


    public List<String> getSizesByType(String type) {
        String sql = "SELECT s.size_value FROM size s JOIN size_types t ON s.size_type_id = t.id WHERE t.name = ?";
        return jdbcTemplate.queryForList(sql, new Object[]{type}, String.class);
    }


    public List<Size> getSizes() {
        String sql = "SELECT * FROM size";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Size size = new Size();
            size.setId(rs.getInt("id"));
            size.setSizeValue(rs.getString("size_value"));
            size.setSizeTypeId(rs.getInt("size_type_id"));
            return size;
        });
    }
}