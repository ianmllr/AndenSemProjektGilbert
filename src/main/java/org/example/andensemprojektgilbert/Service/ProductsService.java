package org.example.andensemprojektgilbert.Service;

import org.example.andensemprojektgilbert.Infrastructure.ProductRepo;
import org.example.andensemprojektgilbert.Model.Product;
import org.example.andensemprojektgilbert.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductsService {

    @Autowired
    private ProductRepo productRepo;

    public List<Product> getAllProducts() {
        if (productRepo.readAllProducts() != null) {
            return productRepo.readAllProducts();
        } else {
            // debug logging her
            return null;
        }
    }

    public List<Product> getMensProducts() {
        if (productRepo.readMensProducts() != null) {
            return productRepo.readMensProducts();
        } else {
            // debug logging her
            return null;
        }
    }
        public List<Product> getWomensProducts() {
        if (productRepo.readWomensProducts() != null) {
            return productRepo.readWomensProducts();
        } else {
            // debug logging her
            return null;
        }
    }

    public List<Product> getMyProducts(User user) {
        if (productRepo.readUserProducts(user) != null) {
            return productRepo.readUserProducts(user);
        } else {
            // debug logging her
            return null;
        }
    }

    public List<Product> getRandomMensProducts() {
        if (productRepo.readRandomMensProducts() != null) {
            return productRepo.readRandomMensProducts();
        } else {
            // debug logging her
            return null;
        }
    }

    public List<Product> getRandomWomensProducts() {
        if (productRepo.readRandomWomensProducts() != null) {
            return productRepo.readRandomWomensProducts();
        } else {
            // debug logging her
            return null;
        }
    }


    public List<Product> getRandomBags() {
        if (productRepo.readRandomBags() != null) {
            return productRepo.readRandomBags();
        } else {
            // debug logging her
            return null;
        }
    }


    public void createProduct(Product product, User user) {
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





}
