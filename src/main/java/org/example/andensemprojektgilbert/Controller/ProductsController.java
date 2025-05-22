package org.example.andensemprojektgilbert.Controller;

import jakarta.servlet.http.HttpSession;
import org.example.andensemprojektgilbert.Model.*;
import org.example.andensemprojektgilbert.Service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        model.addAttribute("departments", productsService.getDepartments());
        model.addAttribute("categories", productsService.getCategories());
        model.addAttribute("subcategories", productsService.getSubcategories());
        model.addAttribute("brands", productsService.getBrands());
        model.addAttribute("locations", productsService.getLocations());
        model.addAttribute("conditions", productsService.getConditions());
        model.addAttribute("colors", productsService.getColors());
        model.addAttribute("sizes", productsService.getSizes());
        model.addAttribute("user", user);
        model.addAttribute("product", new Product());
        return "newproduct";
    }


    @PostMapping("/gilbertprofile/newproduct")
    public String postNewProduct(@ModelAttribute Product product, HttpSession session,
                                 @RequestParam("image") MultipartFile image, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("currentUser");
        product.setCreatedByID(user.getId());

        if (!image.isEmpty()) {
            if (image.getSize() > 3 * 1024 * 1024) {
                redirectAttributes.addFlashAttribute("message", "Image is too large to upload");
                return "redirect:/gilbertprofile/newproduct";
            }
            productsService.createImage(image, product);
        }
        productsService.createProduct(product, user);
        return "redirect:/gilbertprofile";
    }

    @GetMapping("/editproduct/{id}")
    public String getEditProduct(Model model, @PathVariable int id) {
        Product product = productsService.getProduct(id);
        model.addAttribute("product", product);
        model.addAttribute("departments", productsService.getDepartments());
        model.addAttribute("categories", productsService.getCategories());
        model.addAttribute("subcategories", productsService.getSubcategories());
        model.addAttribute("brands", productsService.getBrands());
        model.addAttribute("locations", productsService.getLocations());
        model.addAttribute("conditions", productsService.getConditions());
        model.addAttribute("colors", productsService.getColors());
        model.addAttribute("sizes", productsService.getSizes());
        return "editproduct";
    }

    @PostMapping("/editproduct/{id}")
    public String postEditProduct(@ModelAttribute Product product, HttpSession session,
                                  @RequestParam("image") MultipartFile image, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("currentUser");
        product.setCreatedByID(user.getId());
        if (!image.isEmpty()) {
            if (image.getSize() > 3 * 1024 * 1024) {
                redirectAttributes.addFlashAttribute("message", "Image is too large to upload");
                return "redirect:/gilbertprofile/newproduct";
            }
            productsService.createImage(image, product);
        }
        productsService.updateProduct(product);
        return "redirect:/gilbertprofile";
    }


}