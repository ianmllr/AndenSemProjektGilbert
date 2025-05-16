package org.example.andensemprojektgilbert.Controller;

import jakarta.servlet.http.HttpSession;
import org.example.andensemprojektgilbert.Model.User;
import org.example.andensemprojektgilbert.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AdminController {
    @Autowired
    AdminService adminService;

    @GetMapping("/adminpage")
    public String adminPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if (user.getRole().equals("admin")) {
            return "adminpage";
        }
        model.addAttribute("error", "You are not an admin");
        return "error";
    }
    @GetMapping("/adminusers")
    public String adminUsers(Model model, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if (!user.getRole().equals("admin")) {
            model.addAttribute ("exception", "You must be admin to use this function");
            return "error";
        }
        List<User> users = adminService.getUsers();
        model.addAttribute("users", users);
        return "adminusers";
    }
}
