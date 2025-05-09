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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

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
    public String registerUser(@Valid @ModelAttribute User user, BindingResult bindingResult, HttpSession session, Model model, @RequestParam("image") MultipartFile image, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "register";
        }
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
        if (userService.register(user)) {
            session.setAttribute("currentUser", user);
            return "redirect:/";
        } else {
            model.addAttribute("error", "Email findes allerede");
            return "register";
        }
    }
    @GetMapping("/gilbertprofile")
    public String getUser(Model model, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if (user.getImgsrc() == null || user.getImgsrc().isEmpty()) {
            user.setImgsrc("profil.png");
        } else {
            String uploadDir = "src/main/resources/static/userimage/";
            Path filePath = Paths.get(uploadDir, user.getImgsrc());

            if (!Files.exists(filePath)) {
                user.setImgsrc("profil.png");
            }
        }
        model.addAttribute("user", user);
        return "gilbertprofile";
    }


    @GetMapping("/login")
    public String getLogin(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute User user, BindingResult bindingResult, HttpSession session, Model model) {
        User loggedInUser = userService.login(user.getEmail(), user.getPassword());
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "login";
        }
        if (loggedInUser != null) {
            session.setAttribute("currentUser", loggedInUser);
            return "redirect:/me";
        } else {
            model.addAttribute("error", "Forkert email eller password");
            return "login";
        }
    }
    @GetMapping("/edituser")
    public String getEditUser(Model model, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        model.addAttribute("user", user);
        return "edituser";
    }
    @PostMapping("/edituser")
    public String editUser(@Valid @ModelAttribute User user, BindingResult bindingResult, HttpSession session, Model model, RedirectAttributes redirectAttributes, @RequestParam(value = "image", required = false) MultipartFile image) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "edituser";
        }
        if (image != null && !image.isEmpty() && image.getSize() < 3 * 1024 * 1024) {
        String originalFilename = image.getOriginalFilename();
            String uniqueFilename = UUID.randomUUID().toString() + "_" + originalFilename;
            String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/userimage";
            Path filePath = Paths.get(uploadDir, uniqueFilename);
            try {
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, image.getBytes());
                user.setImgsrc(uniqueFilename);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            User existingUser = (User) session.getAttribute("currentUser");
            user.setImgsrc(existingUser.getImgsrc());
        }
        boolean updated = userService.updateUser(user);
        if (updated) {
            session.setAttribute("currentUser", user);
            return "redirect:/gilbertprofile";
        }
        else {
            redirectAttributes.addFlashAttribute("message", "Der er sket en fejl, prøv igen");
            return "redirect:/edituser";
        }
    }
}
