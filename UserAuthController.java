package com.example.bikerentalsystem.controller;

import com.example.bikerentalsystem.model.User;
import com.example.bikerentalsystem.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("/user")
public class UserAuthController {

    @Autowired
    private UserService userService;

    // ================== AUTH ==================

    // Show login page
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    // Handle login
    @PostMapping("/login")
    public String doLogin(@RequestParam String email,
                          @RequestParam String password,
                          HttpSession session,
                          Model model) throws IOException {

        User user = userService.authenticate(email, password);
        if (user != null) {
            session.setAttribute("user", user);
            return "redirect:/user/profile";
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
    }

    // Show signup form
    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    // Handle signup
    @PostMapping("/signup")
    public String registerUser(@ModelAttribute User user, Model model) throws IOException {
        if (userService.emailExists(user.getEmail())) {
            model.addAttribute("error", "Email already registered");
            return "signup";
        }

        userService.createUser(user);
        return "redirect:/user/login";
    }

    // ================== PROFILE ==================

    // View profile
    @GetMapping("/profile")
    public String viewProfile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/user/login";
        }

        model.addAttribute("user", user);
        return "profile";
    }

    // Show profile edit form
    @GetMapping("/profile/edit")
    public String showProfileEditForm(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/user/login";
        }

        model.addAttribute("user", user);
        return "edit-profile";
    }

    // Handle profile update
    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute User updatedUser,
                                HttpSession session) throws IOException {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            return "redirect:/user/login";
        }

        updatedUser.setId(sessionUser.getId());
        userService.updateUser(updatedUser);
        session.setAttribute("user", updatedUser);

        return "redirect:/user/profile";
    }
    @GetMapping("/profile/delete")
    public String deleteProfile(HttpSession session) throws IOException {
        User user = (User) session.getAttribute("user");

        if (user != null) {
            userService.deleteProfile(user.getId());
            session.invalidate(); // log out after deletion
        }

        return "redirect:/user/login";
    }

    // ================== LOGOUT ==================

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/user/login";
    }
}