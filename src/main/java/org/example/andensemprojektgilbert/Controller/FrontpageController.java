package org.example.andensemprojektgilbert.Controller;

import jakarta.servlet.http.HttpSession;
import org.example.andensemprojektgilbert.Model.Product;
import org.example.andensemprojektgilbert.Model.User;
import org.example.andensemprojektgilbert.Service.FavoriteService;
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
    @Autowired
    private FavoriteService favoriteService;

    @GetMapping("/")
    public String getFrontPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        model.addAttribute("user", user);
        if (user != null) {
            List<Integer> favoriteIds = favoriteService.getFavoriteIds(user.getId());
            model.addAttribute("favorites", favoriteIds);
        }

        List<Product> randomMensProducts = productsService.getRandomMensProducts();
        List<Product> randomWomensProducts = productsService.getRandomWomensProducts();
        List<Product> randomBags = productsService.getRandomBags();
        model.addAttribute("randomMensProducts", randomMensProducts);
        model.addAttribute("randomWomensProducts", randomWomensProducts);
        model.addAttribute("randomBags", randomBags);
        return "index";
    }



    // Går til siden search og søger på hvad ens query er
    @GetMapping("/search")
    public String searchResults(@RequestParam String q, Model model, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        model.addAttribute("user", user);
        model.addAttribute("query", q);
        // Laver en lokal liste af de produkter der, som man søger på
        List<Product> searchProducts = productsService.searchProducts(q);
        // Gør at produkter aldrig kan være null of hvis det ikke findes laver den bare en tom liste.
        model.addAttribute("products", searchProducts != null ? searchProducts : new ArrayList<>());
        return "search";
    }

}
