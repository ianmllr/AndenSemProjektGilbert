package org.example.andensemprojektgilbert.Infrastructure;

import org.example.andensemprojektgilbert.Model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepoImp implements IProductRepo {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ProductRepoImp(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Integer getColorId(String color) {
        String sql = "SELECT idcolor from Color WHERE color = ? LIMIT 1";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, color);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private Integer getSizeId(String sizeValue) {
        String sql = "SELECT id FROM size WHERE size_value = ? LIMIT 1";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{sizeValue}, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private Integer getConditionId(String condition) {
        String sql = "SELECT idcondition FROM gilbert.condition WHERE itemcondition = ? LIMIT 1";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{condition}, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private Integer getBrandId(String brandName) {
        String sql = "SELECT id FROM Brand WHERE name = ? LIMIT 1";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, brandName);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Integer getCategoryId(String categoryName) {
        System.out.println("Looking for category: '" + categoryName + "'");
        String sql = "SELECT id FROM Category WHERE name = ? LIMIT 1";
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
        System.out.println("Looking for subcategory: '" + subcategoryName + "'");
        String sql = "SELECT id FROM Subcategory WHERE name = ? LIMIT 1";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, subcategoryName);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    private Integer getLocationId(String locationName) {
        String sql = "SELECT id FROM Location WHERE name = ? LIMIT 1";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, locationName);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Integer getDepartmentId(String departmentName) {
        String sql = "SELECT id FROM Department WHERE name = ? LIMIT 1";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, departmentName);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // CREATE
    @Override
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
    @Override
    public List<Product> readProductsByDepartmentAndCategory(String department, String category) {
        String sql = "SELECT p.id, p.name, b.name AS brand, l.name AS location, p.description, dcs.department_id, " +
                "dcs.category_id, dcs.subcategory_id, d.name AS department, c.name AS category, sc.name AS subcategory, " +
                "p.posted_date, p.price, cond.itemcondition AS item_condition, s.size_value AS size, col.color AS color, " +
                "p.imgsrc, p.createdbyid " +
                "FROM product p " +
                "INNER JOIN department_category_subcategory dcs " +
                "ON p.category_id = dcs.category_id AND p.subcategory_id = dcs.subcategory_id AND p.department_id = dcs.department_id " +
                "INNER JOIN department d ON dcs.department_id = d.id " +
                "INNER JOIN category c ON dcs.category_id = c.id " +
                "LEFT JOIN subcategory sc ON dcs.subcategory_id = sc.id " +
                "LEFT JOIN brand b ON p.brand_id = b.id " +
                "LEFT JOIN location l ON p.location_id = l.id " +
                "LEFT JOIN color col ON p.color_id = col.idcolor " +
                "LEFT JOIN `condition` cond ON p.condition_id = cond.idcondition " +
                "LEFT JOIN size s ON p.size_id = s.id " +
                "WHERE LOWER(d.name) = ? AND LOWER(c.name) = ?";

        return jdbcTemplate.query(sql, new Object[]{department.toLowerCase(), category.toLowerCase()}, (rs, rowNum) -> {
            Product product = new Product();
            product.setId(rs.getInt("id"));
            product.setName(rs.getString("name"));
            product.setBrand(rs.getString("brand"));
            product.setLocation(rs.getString("location"));
            product.setDescription(rs.getString("description"));
            product.setDepartment(rs.getString("department"));
            product.setCategory(rs.getString("category"));
            product.setSubcategory(rs.getString("subcategory"));
            product.setPostedDate(rs.getTimestamp("posted_date"));
            product.setPrice(rs.getDouble("price"));
            product.setCondition(rs.getString("item_condition"));
            product.setSize(rs.getString("size"));
            product.setColor(rs.getString("color"));
            product.setImgsrc(rs.getString("imgsrc"));
            product.setCreatedByID(rs.getInt("createdbyid"));
            return product;
        });
    }

    @Override
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

    @Override
    public List<Product> searchProducts(String searchTerm) {
        String sql = "SELECT p.id, p.name, b.name AS brand, l.name AS location, p.description, dcs.department_id, " +
                "dcs.category_id, dcs.subcategory_id, d.name AS department, c.name AS category, sc.name AS subcategory, " +
                "p.posted_date, p.price, cond.itemcondition AS item_condition, s.size_value AS size, col.color AS color, " +
                "p.imgsrc, p.createdbyid " +
                "FROM product p " +
                "LEFT JOIN department_category_subcategory dcs ON p.category_id = dcs.category_id AND p.subcategory_id = dcs.subcategory_id AND p.department_id = dcs.department_id " +
                "LEFT JOIN department d ON dcs.department_id = d.id " +
                "LEFT JOIN category c ON dcs.category_id = c.id " +
                "LEFT JOIN subcategory sc ON dcs.subcategory_id = sc.id " +
                "LEFT JOIN brand b ON p.brand_id = b.id " +
                "LEFT JOIN location l ON p.location_id = l.id " +
                "LEFT JOIN color col ON p.color_id = col.idcolor " +
                "LEFT JOIN `condition` cond ON p.condition_id = cond.idcondition " +
                "LEFT JOIN size s ON p.size_id = s.id " +
                "WHERE p.name LIKE ? OR b.name LIKE ? OR l.name LIKE ? OR d.name LIKE ? OR c.name LIKE ? OR sc.name LIKE ? OR p.description LIKE ?";

        String searchPattern = "%" + searchTerm + "%";
        return jdbcTemplate.query(sql, new Object[]{searchPattern, searchPattern, searchPattern, searchPattern, searchPattern, searchPattern, searchPattern},
                (rs, rowNum) -> {
                    Product product = new Product();
                    product.setId(rs.getInt("id"));
                    product.setName(rs.getString("name"));
                    product.setBrand(rs.getString("brand"));
                    product.setLocation(rs.getString("location"));
                    product.setDescription(rs.getString("description"));
                    product.setDepartment(rs.getString("department_id"));
                    product.setCategory(rs.getString("category_id"));
                    product.setSubcategory(rs.getString("subcategory_id"));
                    product.setDepartment(rs.getString("department"));
                    product.setCategory(rs.getString("category"));
                    product.setSubcategory(rs.getString("subcategory"));
                    product.setPostedDate(rs.getTimestamp("posted_date"));
                    product.setPrice(rs.getDouble("price"));
                    product.setCondition(rs.getString("item_condition"));
                    product.setSize(rs.getString("size"));
                    product.setColor(rs.getString("color"));
                    product.setImgsrc(rs.getString("imgsrc"));
                    product.setCreatedByID(rs.getInt("createdbyid"));
                    return product;
                });
    }


    @Override
    public List<Product> readAllProducts() {
        String sql = "SELECT p.id, p.name, b.name AS brand, l.name AS location, p.description, dcs.department_id,\n" +
                "dcs.category_id, dcs.subcategory_id, d.name AS department, \n" +
                "c.name AS category, sc.name AS subcategory, p.posted_date, p.price, \n" +
                "cond.itemcondition AS item_condition, s.size_value AS size, col.color AS color,\n" +
                "p.imgsrc, p.createdbyid\n" +
                "FROM product p\n" +
                "LEFT JOIN department_category_subcategory dcs \n" +
                "ON p.category_id = dcs.category_id AND p.subcategory_id = dcs.subcategory_id AND p.department_id = dcs.department_id\n" +
                "LEFT JOIN department d ON dcs.department_id = d.id\n" +
                "LEFT JOIN category c ON dcs.category_id = c.id\n" +
                "LEFT JOIN subcategory sc ON dcs.subcategory_id = sc.id\n" +
                "LEFT JOIN brand b ON p.brand_id = b.id\n" +
                "LEFT JOIN location l ON p.location_id = l.id\n" +
                "LEFT JOIN color col ON p.color_id = col.idcolor\n" +
                "LEFT JOIN `condition` cond ON p.condition_id = cond.idcondition\n" +
                "LEFT JOIN size s ON p.size_id = s.id";
        return getProducts(sql);
    }

    // læser herre-produkter
    @Override
    public List<Product> readMensProducts() {
        String sql = "SELECT p.id, p.name, b.name AS brand, l.name AS location, p.description, dcs.department_id,\n" +
                "dcs.category_id, dcs.subcategory_id, d.name AS department, \n" +
                "c.name AS category, sc.name AS subcategory, p.posted_date, p.price, \n" +
                "cond.itemcondition AS item_condition, s.size_value AS size, col.color AS color,\n" +
                "p.imgsrc, p.createdbyid\n" +
                "FROM product p\n" +
                "INNER JOIN department_category_subcategory dcs \n" +
                "ON p.category_id = dcs.category_id AND p.subcategory_id = dcs.subcategory_id AND p.department_id = dcs.department_id\n" +
                "INNER JOIN department d ON dcs.department_id = d.id\n" +
                "INNER JOIN category c ON dcs.category_id = c.id\n" +
                "LEFT JOIN subcategory sc ON dcs.subcategory_id = sc.id\n" +
                "LEFT JOIN brand b ON p.brand_id = b.id\n" +
                "LEFT JOIN location l ON p.location_id = l.id\n" +
                "LEFT JOIN color col ON p.color_id = col.idcolor\n" +
                "LEFT JOIN `condition` cond ON p.condition_id = cond.idcondition\n" +
                "LEFT JOIN size s ON p.size_id = s.id\n" +
                "WHERE d.name = 'Men';";
        return getProducts(sql);
    }

    // læser kvinde-produkter
    @Override
    public List<Product> readWomensProducts() {
        String sql = "SELECT p.id, p.name, b.name AS brand, l.name AS location, p.description, dcs.department_id,\n" +
                "dcs.category_id, dcs.subcategory_id, d.name AS department, \n" +
                "c.name AS category, sc.name AS subcategory, p.posted_date, p.price, \n" +
                "cond.itemcondition AS item_condition, s.size_value AS size, col.color AS color,\n" +
                "p.imgsrc, p.createdbyid\n" +
                "FROM product p\n" +
                "INNER JOIN department_category_subcategory dcs \n" +
                "ON p.category_id = dcs.category_id AND p.subcategory_id = dcs.subcategory_id AND p.department_id = dcs.department_id\n" +
                "INNER JOIN department d ON dcs.department_id = d.id\n" +
                "INNER JOIN category c ON dcs.category_id = c.id\n" +
                "LEFT JOIN subcategory sc ON dcs.subcategory_id = sc.id\n" +
                "LEFT JOIN brand b ON p.brand_id = b.id\n" +
                "LEFT JOIN location l ON p.location_id = l.id\n" +
                "LEFT JOIN color col ON p.color_id = col.idcolor\n" +
                "LEFT JOIN `condition` cond ON p.condition_id = cond.idcondition\n" +
                "LEFT JOIN size s ON p.size_id = s.id\n" +
                "WHERE d.name = 'Women'";
        return getProducts(sql);
    }

    @Override
    public List<Product> readHomeProducts() {
        String sql = "SELECT p.id, p.name, b.name AS brand, l.name AS location, p.description, dcs.department_id,\n" +
                "dcs.category_id, dcs.subcategory_id, d.name AS department, \n" +
                "c.name AS category, sc.name AS subcategory, p.posted_date, p.price, \n" +
                "cond.itemcondition AS item_condition, s.size_value AS size, col.color AS color,\n" +
                "p.imgsrc, p.createdbyid\n" +
                "FROM product p\n" +
                "INNER JOIN department_category_subcategory dcs \n" +
                "ON p.category_id = dcs.category_id AND p.subcategory_id = dcs.subcategory_id AND p.department_id = dcs.department_id\n" +
                "INNER JOIN department d ON dcs.department_id = d.id\n" +
                "INNER JOIN category c ON dcs.category_id = c.id\n" +
                "LEFT JOIN subcategory sc ON dcs.subcategory_id = sc.id\n" +
                "LEFT JOIN brand b ON p.brand_id = b.id\n" +
                "LEFT JOIN location l ON p.location_id = l.id\n" +
                "LEFT JOIN color col ON p.color_id = col.idcolor\n" +
                "LEFT JOIN `condition` cond ON p.condition_id = cond.idcondition\n" +
                "LEFT JOIN size s ON p.size_id = s.id\n" +
                "WHERE d.name = 'Home'";
        return getProducts(sql);
    }

    @Override
    public List<Product> readBeautyProducts() {
        String sql = "SELECT p.id, p.name, b.name AS brand, l.name AS location, p.description, dcs.department_id,\n" +
                "dcs.category_id, dcs.subcategory_id, d.name AS department, \n" +
                "c.name AS category, sc.name AS subcategory, p.posted_date, p.price, \n" +
                "cond.itemcondition AS item_condition, s.size_value AS size, col.color AS color,\n" +
                "p.imgsrc, p.createdbyid\n" +
                "FROM product p\n" +
                "INNER JOIN department_category_subcategory dcs \n" +
                "ON p.category_id = dcs.category_id AND p.subcategory_id = dcs.subcategory_id AND p.department_id = dcs.department_id\n" +
                "INNER JOIN department d ON dcs.department_id = d.id\n" +
                "INNER JOIN category c ON dcs.category_id = c.id\n" +
                "LEFT JOIN subcategory sc ON dcs.subcategory_id = sc.id\n" +
                "LEFT JOIN brand b ON p.brand_id = b.id\n" +
                "LEFT JOIN location l ON p.location_id = l.id\n" +
                "LEFT JOIN color col ON p.color_id = col.idcolor\n" +
                "LEFT JOIN `condition` cond ON p.condition_id = cond.idcondition\n" +
                "LEFT JOIN size s ON p.size_id = s.id\n" +
                "WHERE d.name = 'Beauty'";
        return getProducts(sql);
    }

    @Override
    public List<Product> readRandomMensProducts() {
        String sql = "SELECT p.id, p.name, b.name AS brand, l.name AS location, p.description, dcs.department_id,\n" +
                "dcs.category_id, dcs.subcategory_id, d.name AS department, \n" +
                "c.name AS category, sc.name AS subcategory, p.posted_date, p.price, \n" +
                "cond.itemcondition AS item_condition, s.size_value AS size, col.color AS color,\n" +
                "p.imgsrc, p.createdbyid\n" +
                "FROM product p\n" +
                "INNER JOIN department_category_subcategory dcs \n" +
                "ON p.category_id = dcs.category_id AND p.subcategory_id = dcs.subcategory_id AND p.department_id = dcs.department_id\n" +
                "INNER JOIN department d ON dcs.department_id = d.id\n" +
                "INNER JOIN category c ON dcs.category_id = c.id\n" +
                "LEFT JOIN subcategory sc ON dcs.subcategory_id = sc.id\n" +
                "LEFT JOIN brand b ON p.brand_id = b.id\n" +
                "LEFT JOIN location l ON p.location_id = l.id\n" +
                "LEFT JOIN color col ON p.color_id = col.idcolor\n" +
                "LEFT JOIN `condition` cond ON p.condition_id = cond.idcondition\n" +
                "LEFT JOIN size s ON p.size_id = s.id\n" +
                "WHERE d.name = 'Men' ORDER BY RAND() LIMIT 10";
        return getProducts(sql);
    }

    @Override
    public List<Product> readRandomWomensProducts() {
        String sql = "SELECT p.id, p.name, b.name AS brand, l.name AS location, p.description, dcs.department_id,\n" +
                "dcs.category_id, dcs.subcategory_id, d.name AS department, \n" +
                "c.name AS category, sc.name AS subcategory, p.posted_date, p.price, \n" +
                "cond.itemcondition AS item_condition, s.size_value AS size, col.color AS color,\n" +
                "p.imgsrc, p.createdbyid\n" +
                "FROM product p\n" +
                "INNER JOIN department_category_subcategory dcs \n" +
                "ON p.category_id = dcs.category_id AND p.subcategory_id = dcs.subcategory_id AND p.department_id = dcs.department_id\n" +
                "INNER JOIN department d ON dcs.department_id = d.id\n" +
                "INNER JOIN category c ON dcs.category_id = c.id\n" +
                "LEFT JOIN subcategory sc ON dcs.subcategory_id = sc.id\n" +
                "LEFT JOIN brand b ON p.brand_id = b.id\n" +
                "LEFT JOIN location l ON p.location_id = l.id\n" +
                "LEFT JOIN color col ON p.color_id = col.idcolor\n" +
                "LEFT JOIN `condition` cond ON p.condition_id = cond.idcondition\n" +
                "LEFT JOIN size s ON p.size_id = s.id\n" +
                "WHERE d.name = 'Women' ORDER BY RAND() LIMIT 10";
        return getProducts(sql);
    }

    @Override
    public List<Product> readRandomBags() {
        String sql = "SELECT p.id, p.name, b.name AS brand, l.name AS location, p.description, dcs.department_id,\n" +
                "dcs.category_id, dcs.subcategory_id, d.name AS department, \n" +
                "c.name AS category, sc.name AS subcategory, p.posted_date, p.price, \n" +
                "cond.itemcondition AS item_condition, s.size_value AS size, col.color AS color,\n" +
                "p.imgsrc, p.createdbyid\n" +
                "FROM product p\n" +
                "INNER JOIN department_category_subcategory dcs \n" +
                "ON p.category_id = dcs.category_id AND p.subcategory_id = dcs.subcategory_id AND p.department_id = dcs.department_id\n" +
                "INNER JOIN department d ON dcs.department_id = d.id\n" +
                "INNER JOIN category c ON dcs.category_id = c.id\n" +
                "LEFT JOIN subcategory sc ON dcs.subcategory_id = sc.id\n" +
                "LEFT JOIN brand b ON p.brand_id = b.id\n" +
                "LEFT JOIN location l ON p.location_id = l.id\n" +
                "LEFT JOIN color col ON p.color_id = col.idcolor\n" +
                "LEFT JOIN `condition` cond ON p.condition_id = cond.idcondition\n" +
                "LEFT JOIN size s ON p.size_id = s.id\n" +
                "WHERE c.name = 'Bags' ORDER BY RAND() LIMIT 10";
        return getProducts(sql);
    }

    @Override
    public List<Product> readMensProductsBySubCat(String subcategory) {
        String sql = "SELECT p.id, p.name, b.name AS brand, l.name AS location, p.description, d.name AS department,\n" +
                "c.name AS category, sc.name AS subcategory, p.posted_date, p.price, \n" +
                "cond.itemcondition AS `condition`, s.size_value AS size, col.color AS color, \n" +
                "p.imgsrc, p.createdbyid \n" +
                "FROM product p \n" +
                "LEFT JOIN brand b ON p.brand_id = b.id \n" +
                "LEFT JOIN location l ON p.location_id = l.id \n" +
                "LEFT JOIN department_category_subcategory dcs ON p.department_id = dcs.department_id \n" +
                "AND p.category_id = dcs.category_id \n" +
                "AND (p.subcategory_id = dcs.subcategory_id OR dcs.subcategory_id IS NULL)\n" +
                "LEFT JOIN department d ON dcs.department_id = d.id \n" +
                "LEFT JOIN category c ON dcs.category_id = c.id \n" +
                "LEFT JOIN subcategory sc ON dcs.subcategory_id = sc.id \n" +
                "LEFT JOIN color col ON p.color_id = col.idcolor \n" +
                "LEFT JOIN `condition` cond ON p.condition_id = cond.idcondition \n" +
                "LEFT JOIN size s ON p.size_id = s.id \n" +
                "WHERE d.name = 'Men' AND sc.name = ?;";
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

    @Override
    public List<Product> readUserProducts(User user) {
        String sql = "SELECT DISTINCT p.id, p.name, b.name AS brand, l.name AS location, p.description, \n" +
                "d.name AS department, c.name AS category, sc.name AS subcategory, \n" +
                "p.posted_date, p.price, cond.itemcondition AS `condition`, \n" +
                "s.size_value AS size, col.color AS color, p.imgsrc, p.createdbyid \n" +
                "FROM product p \n" +
                "LEFT JOIN brand b ON p.brand_id = b.id \n" +
                "LEFT JOIN location l ON p.location_id = l.id \n" +
                "LEFT JOIN department_category_subcategory dcs \n" +
                "ON p.department_id = dcs.department_id \n" +
                "AND p.category_id = dcs.category_id \n" +
                "AND (p.subcategory_id = dcs.subcategory_id OR dcs.subcategory_id IS NULL)\n" +
                "LEFT JOIN department d ON dcs.department_id = d.id \n" +
                "LEFT JOIN category c ON dcs.category_id = c.id \n" +
                "LEFT JOIN subcategory sc ON dcs.subcategory_id = sc.id \n" +
                "LEFT JOIN color col ON p.color_id = col.idcolor \n" +
                "LEFT JOIN `condition` cond ON p.condition_id = cond.idcondition \n" +
                "LEFT JOIN size s ON p.size_id = s.id \n" +
                "WHERE p.createdbyid = ?;";
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

    @Override
    public Product readProduct(int id) {
        String sql = "SELECT p.id, p.name, b.name AS brand, l.name AS location, p.description, \n" +
                "d.name AS department, c.name AS category, sc.name AS subcategory, \n" +
                "p.posted_date, p.price, cond.itemcondition AS `condition`, s.size_value AS size, \n" +
                "col.color AS color, p.imgsrc, p.createdbyid \n" +
                "FROM product p \n" +
                "LEFT JOIN brand b ON p.brand_id = b.id \n" +
                "LEFT JOIN location l ON p.location_id = l.id \n" +
                "LEFT JOIN department_category_subcategory dcs \n" +
                "ON p.department_id = dcs.department_id \n" +
                "AND p.category_id = dcs.category_id \n" +
                "AND (p.subcategory_id = dcs.subcategory_id OR dcs.subcategory_id IS NULL)\n" +
                "LEFT JOIN department d ON dcs.department_id = d.id \n" +
                "LEFT JOIN category c ON dcs.category_id = c.id \n" +
                "LEFT JOIN subcategory sc ON dcs.subcategory_id = sc.id \n" +
                "LEFT JOIN color col ON p.color_id = col.idcolor \n" +
                "LEFT JOIN `condition` cond ON p.condition_id = cond.idcondition \n" +
                "LEFT JOIN size s ON p.size_id = s.id \n" +
                "WHERE p.id = ?;";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> new Product(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("brand"),
                rs.getString("location"),
                rs.getString("description"),
                rs.getString("department"),
                rs.getString("category"),
                rs.getString("subcategory"),
                rs.getTimestamp("posted_date"),
                rs.getDouble("price"),
                rs.getString("condition"),
                rs.getString("size"),
                rs.getString("color"),
                rs.getString("imgsrc"),
                rs.getInt("createdByID")
        ));
    }

    @Override
    public void updateProduct(Product product) {
        String sql = "UPDATE product SET name = ?, brand_id = ?, location_id = ?, description = ?, " +
                "department_id = ?, category_id = ?, subcategory_id = ?, price = ?, " +
                "condition_id = ?, size_id = ?, color_id = ?, imgsrc = ? WHERE id = ?";
        int brandId = getBrandId(product.getBrand());
        Integer categoryId = getCategoryId(product.getCategory());
        Integer subcategoryId = getSubcategoryId(product.getSubcategory());
        int colorId = getColorId(product.getColor());
        int conditionId = getConditionId(product.getCondition());
        int departmentId = getDepartmentId(product.getDepartment());
        int locationId = getLocationId(product.getLocation());
        Integer sizeId = getSizeId(product.getSize()); // Henter size-id ud fra tekst

        jdbcTemplate.update(sql, product.getName(), brandId, locationId, product.getDescription(),
                departmentId, categoryId, subcategoryId, product.getPrice(),
                conditionId, sizeId, colorId, product.getImgsrc(), product.getId());
    }

    @Override
    public List<Department> getDepartments() {
        String sql = "SELECT id, name FROM Department";
        return jdbcTemplate.query(sql, (resultSet, i) -> {
            Department department = new Department();
            department.setId(resultSet.getInt("id"));
            department.setName(resultSet.getString("name"));
            return department;
        });
    }

    @Override
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

    @Override
    public List<Category> getCategories() {
        String sql = "SELECT * FROM Category";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Category cat = new Category();
            cat.setId(rs.getInt("id"));
            cat.setName(rs.getString("name"));
            return cat;
        });
    }


    @Override
    public List<String> getBrands() {
        String sql ="SELECT name FROM Brand";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    @Override
    public List<String> getLocations() {
        String sql = "SELECT name FROM Location";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    @Override
    public List<String> getConditions() {
        String sql = "SELECT itemcondition FROM `condition`";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    @Override
    public List<String> getColors() {
        String sql = "SELECT color FROM color";
        return jdbcTemplate.queryForList(sql, String.class);
    }


    @Override
    public List<String> getSizesByType(String type) {
        String sql = "SELECT s.size_value FROM size s JOIN size_types t ON s.size_type_id = t.id WHERE t.name = ?";
        return jdbcTemplate.queryForList(sql, new Object[]{type}, String.class);
    }


    @Override
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

    @Override
    public List<Category> findByDepartment(int departmentId) {
        String sql = "SELECT DISTINCT c.id, c.name " +
                "FROM category c " +
                "JOIN department_category_subcategory dcs ON c.id = dcs.category_id " +
                "WHERE dcs.department_id = ?";

        return jdbcTemplate.query(sql, new Object[]{departmentId}, (rs, rowNum) -> {
            Category cat = new Category();
            cat.setId(rs.getInt("id"));
            cat.setName(rs.getString("name"));
            return cat;
        });
    }

    @Override
    public List<Subcategory> findByCategory(int categoryId, int departmentId) {
        String sql = "SELECT DISTINCT sc.id, sc.name, dcs.category_id, sc.size_type_id " +
                "FROM subcategory sc " +
                "JOIN department_category_subcategory dcs ON sc.id = dcs.subcategory_id " +
                "WHERE dcs.category_id = ? AND dcs.department_id = ?";

        return jdbcTemplate.query(sql, new Object[]{categoryId, departmentId}, (rs, rowNum) -> {
            Subcategory sub = new Subcategory();
            sub.setId(rs.getInt("id"));
            sub.setName(rs.getString("name"));
            sub.setCategoryId(rs.getInt("category_id"));
            sub.setSizeTypeId(rs.getInt("size_type_id"));
            return sub;
        });
    }

    @Override
    public boolean deleteProductById(int productId) {
        String sql = "DELETE FROM Product WHERE id = ?";
        int deleted = jdbcTemplate.update(sql, productId);
        if (deleted > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<Product> getProductsPage(int page, int size) {
        String sql = "SELECT \n" +
                "    p.id, p.name, b.name AS brand, l.name AS location, p.description, \n" +
                "    dcs.department_id, dcs.category_id, dcs.subcategory_id, \n" +
                "    d.name AS department, c.name AS category, sc.name AS subcategory, \n" +
                "    p.posted_date, p.price, cond.itemcondition AS item_condition, \n" +
                "    s.size_value AS size, col.color AS color, p.imgsrc, p.createdbyid\n" +
                "FROM product p\n" +
                "LEFT JOIN department_category_subcategory dcs \n" +
                "    ON p.category_id = dcs.category_id \n" +
                "    AND p.subcategory_id = dcs.subcategory_id \n" +
                "    AND p.department_id = dcs.department_id\n" +
                "LEFT JOIN department d ON dcs.department_id = d.id\n" +
                "LEFT JOIN category c ON dcs.category_id = c.id\n" +
                "LEFT JOIN subcategory sc ON dcs.subcategory_id = sc.id\n" +
                "LEFT JOIN brand b ON p.brand_id = b.id\n" +
                "LEFT JOIN location l ON p.location_id = l.id\n" +
                "LEFT JOIN color col ON p.color_id = col.idcolor\n" +
                "LEFT JOIN `condition` cond ON p.condition_id = cond.idcondition\n" +
                "LEFT JOIN size s ON p.size_id = s.id\n" +
                "LIMIT ? OFFSET ?;";
        int offset = (page - 1) * size;
        return jdbcTemplate.query(sql, new Object[]{size, offset}, (rs, rowNum) -> new Product(
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

    @Override
    public boolean createCondition(Condition condition) {
        String sql = "INSERT INTO gilbert.condition(itemcondition) VALUES (?)";
        int result = jdbcTemplate.update(sql, condition.getItemcondition());
        return result == 1;
    }

    @Override
    public boolean createLocation(Location location) {
        String sql = "INSERT INTO location(name) VALUES (?)";
        int result = jdbcTemplate.update(sql, location.getName());
        return result == 1;
    }
}
