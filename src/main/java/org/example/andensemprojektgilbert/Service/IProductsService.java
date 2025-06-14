package org.example.andensemprojektgilbert.Service;

import org.example.andensemprojektgilbert.Model.*;

import java.util.List;

public interface IProductsService {
    List<Product> getProductsByDepartmentAndCategory(String department, String category);
    List<Product> getAllProducts();
    List<Product> getMensProducts();
    List<Product> getWomensProducts();
    List<Product> getHomeProducts();
    List<Product> getBeautyProducts();
    List<Product> getMyProducts(User user);
    Product getProduct(int id);
    List<Product> getRandomMensProducts();
    List<Product> getRandomWomensProducts();
    List<Product> getRandomBags();
    void createProduct(Product product, User user);
    List<Product> searchProducts(String search);
    void updateProduct(Product product);
    List<Department> getDepartments();
    List<Subcategory> getSubcategories();
    List<Category> getCategories();
    List<Size> getSizes();
    List<String> getBrands();
    List<String> getLocations();
    List<String> getConditions();
    List<String> getColors();
    List<String> getSizesByType(String type);
    List<Category> findByDepartment(int departmentId);
    List<Subcategory> findByCategory(int categoryId, int departmentId);
    Department getDepartmentById(int departmentId);
    Category getCategoryById(int categoryId);
    boolean deleteProductById(int productId);
    Integer getDepartmentId(String name);
    Integer getCategoryId(String name);
}
