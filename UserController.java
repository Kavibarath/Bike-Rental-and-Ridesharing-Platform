package com.example.bikerentalsystem.controller;

import com.example.bikerentalsystem.model.User;
import com.example.bikerentalsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Show all users
    @GetMapping
    public String viewAllUsers(Model model) throws IOException {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "users"; // JSP page: users.jsp
    }

    // Show create user form
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        return "create-user"; // JSP page: create-user.jsp
    }

    // Handle create user form submission
    @PostMapping("/create")
    public String createUser(@ModelAttribute User user, Model model) throws IOException {
        userService.createUser(user);
        return "redirect:/users";
    }

    // Show update form
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable int id, Model model) throws IOException {
        List<User> users = userService.getAllUsers();
        User existingUser = users.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElse(null);

        if (existingUser == null) {
            model.addAttribute("error", "User not found");
            return "redirect:/users";
        }

        model.addAttribute("user", existingUser);
        return "edit-user"; // JSP page: edit-user.jsp
    }

    // Handle update form submission
    @PostMapping("/update")
    public String updateUser(@ModelAttribute User user) throws IOException {
        userService.updateUser(user);
        return "redirect:/users";
    }

    // Delete user
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable int id) throws IOException {
        userService.deleteUser(id);
        return "redirect:/users";
    }
}