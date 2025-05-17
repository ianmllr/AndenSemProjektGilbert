package org.example.andensemprojektgilbert.Controller;

import jakarta.servlet.http.HttpSession;
import org.example.andensemprojektgilbert.Model.User;
import org.example.andensemprojektgilbert.Service.AdminService;
import org.example.andensemprojektgilbert.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    AdminService adminService;

    @Autowired
    UserService userService;

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
    public String adminUsers(@RequestParam(defaultValue = "1") int page, Model model, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if (!user.getRole().equals("admin")) {
            model.addAttribute ("exception", "You must be admin to use this function");
            return "error";
        }
        int pageSize = 10;
        List<User> users = adminService.getUsersPage(page, pageSize);
        int totalPages = (users.size() + pageSize) / pageSize;
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("users", users);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", pageSize);
        return "adminusers";
    }
    @GetMapping("/edituseradmin/{id}")
    public String editUserAdmin(@PathVariable int id, Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        User user = adminService.getUser(id);
        if (user != null && currentUser.getRole().equals("admin")) {
            model.addAttribute("user", user);
            return "/edituseradmin";
        }
        model.addAttribute("exception", "Cannot find user");
        return "error";
    }

    @PostMapping("/edituseradmin")
    public String editUserAdmin(@ModelAttribute User user, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        boolean updated = userService.updateUser(user);
        if (updated) {
            redirectAttributes.addFlashAttribute("succes", "The information has been updated");
            return "redirect:/adminusers";
        } else {
            redirectAttributes.addFlashAttribute("error", "Error updating user");
            return "redirect:/adminusers";
        }
    }
@GetMapping("/adminpage/giveadminrights/{id}")
public String adminRights(@PathVariable int id, Model model, HttpSession session, User user, RedirectAttributes redirectAttributes) {
    user = (User) session.getAttribute("currentUser");
    if (user.getRole().equals("admin")) {
        boolean adminrights = adminService.giveAdminRights(id);
        if (adminrights) {
            redirectAttributes.addFlashAttribute("succes", "Admin rights have been granted");
            return "redirect:/adminusers";
        }
        redirectAttributes.addFlashAttribute("error", "Admin rights have not been granted");
        return "redirect:/adminusers";
    }
    model.addAttribute("exception", "Cannot grant admin rights");
    return "error";
}
    @GetMapping("/adminpage/removeadminrights/{id}")
    public String removeAdminRights(@PathVariable int id, Model model, HttpSession session, User user, RedirectAttributes redirectAttributes) {
        user = (User) session.getAttribute("currentUser");
        if (user.getRole().equals("admin") && user.getId() != id) {
            boolean adminrights = adminService.removeAdminRights(id);
            if (adminrights) {
                redirectAttributes.addFlashAttribute("succes", "Admin rights have been rewoked");
                return "redirect:/adminusers";
            }
            redirectAttributes.addFlashAttribute("error", "Admin rights have not been rewoked");
            return "redirect:/adminusers";
        }
        redirectAttributes.addFlashAttribute("error", "Admin rights cannot be rewoked from own user");
        return "redirect:/adminusers";
    }
    @GetMapping("/searchForUser")
    public String searchForUser(@RequestParam("query") String query, Model model, HttpSession session) {
        List<User> filteredUsers = adminService.searchForUser(query);
        model.addAttribute("users", filteredUsers);
        model.addAttribute("currentPage", 1);
        return "adminusers";
    }
    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        boolean deleted = adminService.deleteUser(id);
        if (deleted) {
            return ResponseEntity.status(HttpStatus.OK).body("Deleted");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
