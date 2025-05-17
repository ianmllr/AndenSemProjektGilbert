package org.example.andensemprojektgilbert.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.andensemprojektgilbert.Model.Product;
import org.example.andensemprojektgilbert.Model.User;
import org.example.andensemprojektgilbert.Service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class FavoriteController {
    @Autowired
    private FavoriteService favoriteService;
    @GetMapping("/favorites")
    public String showFavorites(Model model, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            return "redirect:/login";
        }
        List<Product> favorites = favoriteService.getFavorites(user.getId());
        model.addAttribute("favorites", favorites);
        return "favorites";
    }
    @PostMapping("/products/addToFavorites/{id}")
    public String addFavorite(@PathVariable int id, RedirectAttributes redirectAttributes, HttpSession session, HttpServletRequest request) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            return "redirect:/login";
        }
        boolean isFavorite = favoriteService.addFavorite(user.getId(), id);
        if (isFavorite) {
            redirectAttributes.addFlashAttribute("success", "Added favorite product");
        }
        else {
            redirectAttributes.addFlashAttribute("error", "Something went wrong. Try again");
        }
        String referrer = request.getHeader("Referer");
        return "redirect:" + (referrer != null ? referrer : "/");
    }
    @PostMapping("/products/removeAsFavorite/{id}")
    public String removeFavorite(@PathVariable int id, RedirectAttributes redirectAttributes, HttpSession session, HttpServletRequest request) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            return "redirect:/login";
        }
        boolean removeAsFavorite = favoriteService.removeFavorite(user.getId(), id);
        if (removeAsFavorite) {
            redirectAttributes.addFlashAttribute("success", "Removed favorite product");
        }
        else {
            redirectAttributes.addFlashAttribute("error", "Something went wrong. Try again");
        }
        String referrer = request.getHeader("Referer");
        return "redirect:" + (referrer != null ? referrer : "/");
    }
}