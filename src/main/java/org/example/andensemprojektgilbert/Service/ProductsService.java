package org.example.andensemprojektgilbert.Service;

import org.example.andensemprojektgilbert.Infrastructure.ProductRepo;
import org.example.andensemprojektgilbert.Model.Product;
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

    public List<Product> searchProduct(String searchText) {
       return  productRepo.searchProducts(searchText);
    }





}
