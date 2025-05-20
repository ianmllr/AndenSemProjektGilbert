package org.example.andensemprojektgilbert.Controller;

import jakarta.servlet.http.HttpSession;
import org.example.andensemprojektgilbert.Model.*;
import org.example.andensemprojektgilbert.Service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
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
import java.util.Collections;
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
    public String getNewProduct(@RequestParam(value = "departmentId", required = false) Integer departmentId,
                                @RequestParam(value = "categoryId", required = false) Integer categoryId,
                                Model model, HttpSession session) {

        User user = (User) session.getAttribute("currentUser");
        model.addAttribute("departments", productsService.getDepartments());

        List<Category> categories = (departmentId != null) ? productsService.findByDepartment(departmentId) : Collections.emptyList();
        model.addAttribute("categories", categories);

        List<Subcategory> subcategories = (categoryId != null) ? productsService.findByCategory(categoryId, departmentId) : Collections.emptyList();
        model.addAttribute("subcategories", subcategories);

        model.addAttribute("selectedDepartmentId", departmentId);
        model.addAttribute("selectedCategoryId", categoryId);

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
    public String postNewProduct(@ModelAttribute Product product,
                                 @RequestParam(value = "departmentId", required = false) Integer departmentId,
                                 @RequestParam(value = "categoryId", required = false) Integer categoryId,
                                 HttpSession session,
                                 Model model,
                                 @RequestParam("image") MultipartFile image,
                                 RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("currentUser");

        System.out.println("Received departmentId: " + departmentId);
        System.out.println("Received categoryId: " + categoryId);

        Department department = (departmentId != null) ? productsService.getDepartmentById(departmentId) : null;
        Category category = (categoryId != null) ? productsService.getCategoryById(categoryId) : null;

        product.setDepartment(department.getName());
        product.setCategory(category.getName());
        product.setCreatedByID(user.getId());

        if (!image.isEmpty()) {
            if (image.getSize() > 3 * 1024 * 1024) {
                redirectAttributes.addFlashAttribute("message", "Image is too large to upload");
                return "redirect:/index";
            }

            String originalFilename = image.getOriginalFilename();
            String uniqueFilename = UUID.randomUUID().toString() + "_" + originalFilename;
            String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/productimage";
            Path filePath = Paths.get(uploadDir, uniqueFilename);

            try {
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, image.getBytes());
                product.setImgsrc(uniqueFilename);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        productsService.createProduct(product, user);
        return "redirect:/gilbertprofile";
    }

    @PostMapping("/gilbertprofile/deleteproduct")
    public String deleteProduct(@RequestParam("productId") int productId,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            redirectAttributes.addFlashAttribute("message", "Du skal være logget ind for at slette et produkt.");
            return "redirect:/login";
        }

        Product product = productsService.getProduct(productId);
        if (product != null && product.getCreatedByID() == user.getId()) {
            productsService.deleteProductById(productId);
            redirectAttributes.addFlashAttribute("message", "Produktet blev slettet.");
        } else {
            redirectAttributes.addFlashAttribute("message", "Du har ikke tilladelse til at slette dette produkt, eller produktet blev ikke fundet.");
        }

        return "redirect:/gilbertprofile";
    }

}