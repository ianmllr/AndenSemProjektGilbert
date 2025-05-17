package org.example.andensemprojektgilbert.Service;

import org.example.andensemprojektgilbert.Infrastructure.ProductRepo;
import org.example.andensemprojektgilbert.Model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductsService {

    @Autowired
    private ProductRepo productRepo;

    public List<Product> getAllProducts() {
        if (!productRepo.readAllProducts().isEmpty()) {
            return productRepo.readAllProducts();
        } else {
            // debug logging her
            return null;
        }
    }

    public List<Product> getMensProducts() {
        if (!productRepo.readMensProducts().isEmpty()) {
            return productRepo.readMensProducts();
        } else {
            // debug logging her
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
    public List<Product> getWomensProducts() {
        if (!productRepo.readWomensProducts().isEmpty()) {
            return productRepo.readWomensProducts();
        } else {
            System.out.println("Womens products er tom");
            return null;
        }
    }

    public List<Product> getMyProducts(User user) {
        if (!productRepo.readUserProducts(user).isEmpty()) {
            return productRepo.readUserProducts(user);
        } else {
            // debug logging her
            return null;
        }
    }

    public Product getProduct(int id) {
        if (productRepo.readProduct(id) != null) {
            return productRepo.readProduct(id);
        } else {
            // debug logging her
            return null;
        }
    }

    public List<Product> getRandomMensProducts() {
        if (!productRepo.readRandomMensProducts().isEmpty()) {
            return productRepo.readRandomMensProducts();
        } else {
            // debug logging her
            return null;
        }
    }

    public List<Product> getRandomWomensProducts() {
        if (!productRepo.readRandomWomensProducts().isEmpty()) {
            return productRepo.readRandomWomensProducts();
        } else {
            // debug logging her
            return null;
        }
    }


    public List<Product> getRandomBags() {
        if (!productRepo.readRandomBags().isEmpty()) {
            return productRepo.readRandomBags();
        } else {
            // debug logging her
            return null;
        }
    }


    public void createProduct(Product product, User user) {
        System.out.println(product.toString());
        if (product != null) {
            product.setPostedDate(new java.util.Date());
            productRepo.createProduct(product, user);
        } else {
            // debug logging her
        }
    }

    public List<Product> searchProducts(String search) {
        return productRepo.searchProducts(search);
    }


    public void updateProduct(Product product) {
        if (product != null) {
            productRepo.updateProduct(product);
        }
    }

    public List<Department> getDepartments() {
        return productRepo.getDepartments();
    }

    public List<Subcategory> getSubcategories() {
        return productRepo.getSubcategories();
    }

    public List<Category> getCategories() {
        return productRepo.getCategories();
    }

    public List<Size> getSizes() {
        return productRepo.getSizes();
    }

    public List<String> getBrands() {
        return productRepo.getBrands();
    }

    public List<String> getLocations() {
        return productRepo.getLocations();
    }

    public List<String> getConditions() {
        return productRepo.getConditions();
    }

    public List<String> getColors() {
        return productRepo.getColors();
    }


    public List<String> getSizesByType(String type) {
        return productRepo.getSizesByType(type);
    }
    public List<Category> findByDepartment(int departmentId) {
        return productRepo.findByDepartment(departmentId);
    }
    public List<Subcategory> findByCategory(int categoryId, int departmentId) {
        return productRepo.findByCategory(categoryId, departmentId);
    }

}