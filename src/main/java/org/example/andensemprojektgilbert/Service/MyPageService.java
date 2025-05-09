package org.example.andensemprojektgilbert.Service;

import org.example.andensemprojektgilbert.Infrastructure.ProductRepo;
import org.example.andensemprojektgilbert.Infrastructure.UserRepo;
import org.example.andensemprojektgilbert.Model.Product;
import org.example.andensemprojektgilbert.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyPageService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ProductRepo productRepo;

    public List<Product> getMyProducts(User user) {
        if (productRepo.readUserProducts(user) != null) {
            return productRepo.readUserProducts(user);
        } else {
            // debug logging her
            return null;
        }
    }

    public void createProduct(Product product) {
        productRepo.createProduct(product);
    }

    // fjern fra favorit
    // rediger profil
    // osv osv osv osv


}
