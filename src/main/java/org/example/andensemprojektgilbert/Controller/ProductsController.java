package org.example.andensemprojektgilbert.Controller;

import jakarta.servlet.http.HttpSession;
import org.example.andensemprojektgilbert.Model.Product;
import org.example.andensemprojektgilbert.Model.User;
import org.example.andensemprojektgilbert.Service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class ProductsController {

    @Autowired
    private ProductsService productsService;

    @GetMapping("/men")
    public String getMensProducts(Model model) {
        List<Product> products = productsService.getMensProducts();
        model.addAttribute("men", products);
        return "men";
    }

    // women, home osv her
    //
    //

    @GetMapping("/gilbertprofile/newproduct")
    public String getNewProduct(Model model, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        System.out.println("opened new product page with user " + user.getName());
        model.addAttribute("user", user);
        model.addAttribute("product", new Product());
        return "newproduct";
    }

    @PostMapping("/gilbertprofile/newproduct")
    public String postNewProduct(@ModelAttribute Product product, HttpSession session, Model model, @RequestParam("image") MultipartFile image, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("currentUser");
        model.addAttribute("product", product);
        product.setCreatedByID(user.getId());
        productsService.createProduct(product);

        if (!image.isEmpty()) {
            if (image.getSize() > 3*1024*1024) {
                redirectAttributes.addFlashAttribute("message", "Image is too large to upload");
                return "redirect:/register";
            }
            String originalFilename = image.getOriginalFilename();
            String uniqueFilename = UUID.randomUUID().toString() + "_" + originalFilename;
            String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/userimage";;
            Path filePath = Paths.get(uploadDir, uniqueFilename);
            try {
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, image.getBytes());
                user.setImgsrc(uniqueFilename);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return "redirect:/gilbertprofile";
    }



}
