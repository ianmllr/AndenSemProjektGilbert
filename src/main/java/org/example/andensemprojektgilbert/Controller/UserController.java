package org.example.andensemprojektgilbert.Controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.andensemprojektgilbert.Model.User;
import org.example.andensemprojektgilbert.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private UserService userService = new UserService();

    @GetMapping("/register")
    public String getRegister(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute User user, BindingResult bindingResult, HttpSession session, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "register";
        }
        if (userService.register(user)) {
            session.setAttribute("currentUser", user);
            return "redirect:/index";
        } else {
            model.addAttribute("error", "Email findes allerede");
            return "register";
        }
    }
    @GetMapping("/gilbertprofile")
    public String getUser(Model model) {
        model.addAttribute("user", new User());
        return "gilbertprofile";
    }
}
