package org.example.andensemprojektgilbert.Infrastructure;

import org.example.andensemprojektgilbert.Model.*;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;

public interface IProductRepo {
    Integer getCategoryId(String categoryName);
    Integer getDepartmentId(String departmentName);
    void createProduct(Product product, User user);
    List<Product> readProductsByDepartmentAndCategory(String department, String category);
    List<Product> getProducts(String sql);
    List<Product> searchProducts(String searchTerm);
    List<Product> readAllProducts();
    List<Product> readMensProducts();
    List<Product> readWomensProducts();
    List<Product> readHomeProducts();
    List<Product> readBeautyProducts();
    List<Product> readRandomMensProducts();
    List<Product> readRandomWomensProducts();
    List<Product> readRandomBags();
    List<Product> readMensProductsBySubCat(String subcategory);
    List<Product> readUserProducts(User user);
    Product readProduct(int id);
    void updateProduct(Product product);
    List<Department> getDepartments();
    List<Subcategory> getSubcategories();
    List<Category> getCategories();
    List<String> getBrands();
    List<String> getLocations();
    List<String> getConditions();
    List<String> getColors();
    List<String> getSizesByType(String type);
    List<Size> getSizes();
    List<Category> findByDepartment(int departmentId);
    List<Subcategory> findByCategory(int categoryId, int departmentId);
    boolean deleteProductById(int productId);
    List<Product> getProductsPage(int page, int size);
    boolean createCondition(Condition condition);
    boolean createLocation(Location location);

}
