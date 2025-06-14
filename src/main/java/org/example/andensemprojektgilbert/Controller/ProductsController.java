package org.example.andensemprojektgilbert.Controller;

import jakarta.servlet.http.HttpSession;
import org.example.andensemprojektgilbert.Model.*;
import org.example.andensemprojektgilbert.Service.FavoriteServiceImp;
import org.example.andensemprojektgilbert.Service.IFavoriteService;
import org.example.andensemprojektgilbert.Service.IProductsService;
import org.example.andensemprojektgilbert.Service.ProductsServiceImp;
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
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Controller
public class ProductsController {

    private final IProductsService productsService;
    private final IFavoriteService favoriteService;

    @Autowired
    public ProductsController(IProductsService productsService, IFavoriteService favoriteService) {
        this.productsService = productsService;
        this.favoriteService = favoriteService;
    }

    @GetMapping("/men")
    public String getMensProducts(Model model, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        model.addAttribute("user", user);
        List<Product> products = productsService.getMensProducts();
        model.addAttribute("products", products);
        if (user != null) {
            List<Integer> favoriteIds = favoriteService.getFavoriteIds(user.getId());
            model.addAttribute("favorites", favoriteIds);
        }
        return "men";
    }


    @GetMapping("/women")
    public String getWomensProducts(Model model, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        model.addAttribute("user", user);
        List<Product> products = productsService.getWomensProducts();
        model.addAttribute("products", products);
        if (user != null) {
            List<Integer> favoriteIds = favoriteService.getFavoriteIds(user.getId());
            model.addAttribute("favorites", favoriteIds);
        }
        return "women";
    }

    @GetMapping("/home")
    public String getHomeProducts(Model model, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        model.addAttribute("user", user);
        List<Product> products = productsService.getHomeProducts();
        model.addAttribute("products", products);
        if (user != null) {
            List<Integer> favoriteIds = favoriteService.getFavoriteIds(user.getId());
            model.addAttribute("favorites", favoriteIds);
        }
        return "home";
    }

    @GetMapping("/beauty")
    public String getBeautyProducts(Model model, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        model.addAttribute("user", user);
        List<Product> products = productsService.getBeautyProducts();
        model.addAttribute("products", products);
        if (user != null) {
            List<Integer> favoriteIds = favoriteService.getFavoriteIds(user.getId());
            model.addAttribute("favorites", favoriteIds);
        }
        return "beauty";
    }

    @GetMapping("/products/{department}/{category}")
    public String getDepartmentCategoryProducts(@PathVariable String department,
                                                @PathVariable String category,
                                                Model model,
                                                HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        model.addAttribute("user", user);

        List<Product> products = productsService.getProductsByDepartmentAndCategory(
                department.toLowerCase(), category.toLowerCase()
        );
        model.addAttribute("products", products);

        if (user != null) {
            List<Integer> favoriteIds = favoriteService.getFavoriteIds(user.getId());
            model.addAttribute("favorites", favoriteIds);
        }

        model.addAttribute("department", department);
        model.addAttribute("category", category);

        return "category-view";
    }






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
                                 @RequestParam("image") MultipartFile image,
                                 RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("currentUser");

        System.out.println("Received departmentId: " + departmentId);
        System.out.println("Received categoryId: " + categoryId);

        Department department = (departmentId != null) ? productsService.getDepartmentById(departmentId) : null;
        Category category = (categoryId != null) ? productsService.getCategoryById(categoryId) : null;

        product.setDepartment(department.getName());
        if (category != null) {
            product.setCategory(category.getName());
        }
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
            redirectAttributes.addFlashAttribute("message", "You must be logged in to delete a product.");
            return "redirect:/login";
        }

        Product product = productsService.getProduct(productId);
        if (product != null && product.getCreatedByID() == user.getId()) {
            productsService.deleteProductById(productId);
            redirectAttributes.addFlashAttribute("message", "Product deleted.");
        } else {
            redirectAttributes.addFlashAttribute("message", "You do not have the permission to delete this product or it was not found.");
        }

        return "redirect:/gilbertprofile";
    }



    @GetMapping("/editproduct/{id}")
    public String getEditProduct(@PathVariable int id,
                                 @RequestParam(value = "departmentId", required = false) Integer departmentId,
                                 @RequestParam(value = "categoryId", required = false) Integer categoryId,
                                 Model model, HttpSession session) {

        User user = (User) session.getAttribute("currentUser");
        Product product = productsService.getProduct(id);

        if (product == null || product.getCreatedByID() != user.getId()) {
            model.addAttribute("exception", "Unauthorized or product not found");
            return "error";
        }

        departmentId = (departmentId != null) ? departmentId : productsService.getDepartmentId(product.getDepartment());
        categoryId = (categoryId != null) ? categoryId : productsService.getCategoryId(product.getCategory());

        List<Category> categories = (departmentId != null) ? productsService.findByDepartment(departmentId) : Collections.emptyList();
        List<Subcategory> subcategories = (categoryId != null) ? productsService.findByCategory(categoryId, departmentId) : Collections.emptyList();
        System.out.println(product.getPostedDate());
        model.addAttribute("product", product);
        model.addAttribute("departments", productsService.getDepartments());
        model.addAttribute("categories", categories);
        model.addAttribute("subcategories", subcategories);
        model.addAttribute("brands", productsService.getBrands());
        model.addAttribute("locations", productsService.getLocations());
        model.addAttribute("conditions", productsService.getConditions());
        model.addAttribute("colors", productsService.getColors());
        model.addAttribute("sizes", productsService.getSizes());
        model.addAttribute("user", user);
        model.addAttribute("selectedDepartmentId", departmentId);
        model.addAttribute("selectedCategoryId", categoryId);

        return "editproduct";
    }
    @PostMapping("/editproduct/{id}")
    public String updateProduct(@ModelAttribute Product product,
                                 @RequestParam(value = "departmentId", required = false) Integer departmentId,
                                 @RequestParam(value = "categoryId", required = false) Integer categoryId,
                                 @RequestParam("image") MultipartFile image,
                                 RedirectAttributes redirectAttributes) {

        System.out.println("Received departmentId: " + departmentId);
        System.out.println("Received categoryId: " + categoryId);

        Department department = (departmentId != null) ? productsService.getDepartmentById(departmentId) : null;
        Category category = (categoryId != null) ? productsService.getCategoryById(categoryId) : null;

        product.setDepartment(department.getName());
        if (category != null) {
            product.setCategory(category.getName());
        }

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
        productsService.updateProduct(product);
        return "redirect:/gilbertprofile";
    }
}