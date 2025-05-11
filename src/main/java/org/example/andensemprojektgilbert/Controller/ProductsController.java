package org.example.andensemprojektgilbert.Controller;

import org.example.andensemprojektgilbert.Model.Product;
import org.example.andensemprojektgilbert.Service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ProductsController {

    @Autowired
    private ProductsService productsService;

    @GetMapping("/men")
    public String getMen(Model model) {
        List<Product> products = productsService.getMensProducts();
        model.addAttribute("men", products);
        return "men";
    }


}
