package org.example.andensemprojektgilbert.Controller;

import org.example.andensemprojektgilbert.Model.Product;
import org.example.andensemprojektgilbert.Service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class FrontpageController {

    @Autowired
    private ProductsService productsService;

    @GetMapping("/")
    public String frontpage() {
        return "index";
    }



    // Går til siden search og søger på hvad ens query er
    @GetMapping("/search")
    public String searchResults(@RequestParam String q, Model model) {
        model.addAttribute("query", q);
        // Laver en lokal liste af de produkter der, som man søger på
        List<Product> searchProducts = productsService.searchProduct(q);
        // Gør at produkter aldrig kan være null of hvis det ikke findes laver den bare en tom liste.
        model.addAttribute("products", searchProducts != null ? searchProducts : new ArrayList<>());
        return "search";
    }

}
