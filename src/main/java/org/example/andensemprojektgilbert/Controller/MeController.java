package org.example.andensemprojektgilbert.Controller;

import jakarta.servlet.http.HttpSession;
import org.example.andensemprojektgilbert.Model.Product;
import org.example.andensemprojektgilbert.Model.User;
import org.example.andensemprojektgilbert.Service.MyPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class MeController {

    @Autowired
    private MyPageService myPageService;

    @GetMapping("/me")
    public String getMyPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        model.addAttribute("user", user);
        List<Product> products = myPageService.getMyProducts(user);
        model.addAttribute("products", products);
        return "me";
    }

    @GetMapping("/me/newproduct")
    public String getNewProduct(Model model, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        System.out.println("opened new product page with user " + user.getName());
        model.addAttribute("user", user);
        model.addAttribute("product", new Product());
        return "newproduct";
    }

    @PostMapping("/me/newproduct")
    public String postNewProduct(@ModelAttribute Product product, HttpSession session, Model model) {
        User user = (User) session.getAttribute("currentUser");
        model.addAttribute("product", product);
        product.setCreatedByID(user.getId());
        myPageService.createProduct(product);
        return "redirect:/me";

    }

}
