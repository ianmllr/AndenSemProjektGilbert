package org.example.andensemprojektgilbert.Controller;

import jakarta.servlet.http.HttpSession;
import org.example.andensemprojektgilbert.Model.Product;
import org.example.andensemprojektgilbert.Model.User;
import org.example.andensemprojektgilbert.Service.MyPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class MeController {

    @Autowired
    private MyPageService myPageService = new MyPageService();

    @GetMapping("/me")
    public String getMyPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        List<Product> products = myPageService.getMyProducts(user);
        model.addAttribute("products", products);
        return "me";
    }

    @GetMapping("/me/newProduct")
    public String getNewProduct(Model model) {
        model.addAttribute("product", new Product());
        return "/me/newProduct";
    }

    @PostMapping("/me/newProduct")
    public String postNewProduct(HttpSession session, Product product) {
        User user = (User) session.getAttribute("currentUser");
        product.setCreatedByID(user.getId());
        myPageService.createProduct(product);
        return "redirect:/me";

    }

}
