package org.example.andensemprojektgilbert.Service;

import org.example.andensemprojektgilbert.Infrastructure.IProductRepo;
import org.example.andensemprojektgilbert.Infrastructure.ProductRepoImp;
import org.example.andensemprojektgilbert.Model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ProductsServiceImp implements IProductsService {

    private final IProductRepo productRepo;

    @Autowired
    public ProductsServiceImp(IProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public List<Product> getProductsByDepartment(String department) {
        return switch (department.toLowerCase()) {
            case "men" -> getMensProducts();
            case "women" -> getWomensProducts();
            case "beauty" -> getBeautyProducts();
            case "home" -> getHomeProducts();
            default -> Collections.emptyList();
        };
    }

    @Override
    public List<Product> getProductsByDepartmentAndCategory(String department, String category) {
        return productRepo.readProductsByDepartmentAndCategory(department, category);
    }

    @Override
    public List<Product> getAllProducts() {
        if (!productRepo.readAllProducts().isEmpty()) {
            return productRepo.readAllProducts();
        } else {
            System.out.println("Ingen produkter fundet");
            return null;
        }
    }

    @Override
    public List<Product> getMensProducts() {
        if (!productRepo.readMensProducts().isEmpty()) {
            return productRepo.readMensProducts();
        } else {
            System.out.println("Mens products er tom");
            return null;
        }
    }

    @Override
    public List<Product> getWomensProducts() {
        if (!productRepo.readWomensProducts().isEmpty()) {
            return productRepo.readWomensProducts();
        } else {
            System.out.println("Womens products er tom");
            return null;
        }
    }

    @Override
    public List<Product> getHomeProducts() {
        if (!productRepo.readHomeProducts().isEmpty()) {
            return productRepo.readHomeProducts();
        } else {
            System.out.println("Home products er tom");
            return null;
        }
    }

    @Override
    public List<Product> getBeautyProducts() {
        List<Product> products = productRepo.readBeautyProducts();
        if (!products.isEmpty()) {
            return products;
        } else {
            System.out.println("Beauty products er tom");
            return null;
        }
    }

    public List<Product> getMensProductsBySubCategory(String subcategory) {
        if (productRepo.readMensProductsBySubCat(subcategory) != null) {
            return productRepo.readMensProductsBySubCat(subcategory);} else {
            // debug logging her
            return null;
        }
    }

    @Override
    public List<Product> getMyProducts(User user) {
        if (!productRepo.readUserProducts(user).isEmpty()) {
            return productRepo.readUserProducts(user);
        } else {
            // debug logging her
            return null;
        }
    }

    @Override
    public Product getProduct(int id) {
        if (productRepo.readProduct(id) != null) {
            return productRepo.readProduct(id);
        } else {
            // debug logging her
            return null;
        }
    }

    @Override
    public List<Product> getRandomMensProducts() {
        if (!productRepo.readRandomMensProducts().isEmpty()) {
            return productRepo.readRandomMensProducts();
        } else {
            // debug logging her
            return null;
        }
    }

    @Override
    public List<Product> getRandomWomensProducts() {
        if (!productRepo.readRandomWomensProducts().isEmpty()) {
            return productRepo.readRandomWomensProducts();
        } else {
            // debug logging her
            return null;
        }
    }

    @Override
    public List<Product> getRandomBags() {
        if (!productRepo.readRandomBags().isEmpty()) {
            return productRepo.readRandomBags();
        } else {
            // debug logging her
            return null;
        }
    }

    @Override
    public void createProduct(Product product, User user) {
        System.out.println(product.toString());
        if (product != null) {
            product.setPostedDate(new java.util.Date());
            System.out.println(product.getPostedDate());
            productRepo.createProduct(product, user);
        } else {
            // debug logging her
        }
    }

    @Override
    public List<Product> searchProducts(String search) {
        return productRepo.searchProducts(search);
    }

    @Override
    public void updateProduct(Product product) {
        if (product != null) {
            productRepo.updateProduct(product);
        }
    }

    @Override
    public List<Department> getDepartments() {
        return productRepo.getDepartments();
    }

    @Override
    public List<Subcategory> getSubcategories() {
        return productRepo.getSubcategories();
    }

    @Override
    public List<Category> getCategories() {
        return productRepo.getCategories();
    }

    @Override
    public List<Size> getSizes() {
        return productRepo.getSizes();
    }

    @Override
    public List<String> getBrands() {
        return productRepo.getBrands();
    }

    @Override
    public List<String> getLocations() {
        return productRepo.getLocations();
    }

    @Override
    public List<String> getConditions() {
        return productRepo.getConditions();
    }

    @Override
    public List<String> getColors() {
        return productRepo.getColors();
    }

    @Override
    public List<String> getSizesByType(String type) {
        return productRepo.getSizesByType(type);
    }

    @Override
    public List<Category> findByDepartment(int departmentId) {
        return productRepo.findByDepartment(departmentId);
    }

    @Override
    public List<Subcategory> findByCategory(int categoryId, int departmentId) {
        return productRepo.findByCategory(categoryId, departmentId);
    }

    @Override
    public Department getDepartmentById(int departmentId) {
        List <Department> departments = getDepartments();
        for (Department department : departments) {
            if (department.getId() == departmentId) {
                return department;
            }
        }
        return null;
    }

    @Override
    public Category getCategoryById(int categoryId) {
        List <Category> categories = getCategories();
        for (Category category : categories) {
            if (category.getId() == categoryId) {
                return category;
            }
        }
        return null;
    }

    @Override
    public boolean deleteProductById(int productId) {
        Product product = getProduct(productId);
        if (product != null) {
            productRepo.deleteProductById(productId);
        } else {
            // Debug logging eller håndtering af ikke-eksisterende produkt
            System.out.println("Produkt med ID " + productId + " blev ikke fundet.");
        }
        return false;
    }

    @Override
    public Integer getDepartmentId(String name) {
        return productRepo.getDepartmentId(name);
    }

    @Override
    public Integer getCategoryId(String name) {
        return productRepo.getCategoryId(name);
    }
}