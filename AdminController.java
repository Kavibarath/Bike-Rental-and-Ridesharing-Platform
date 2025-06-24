package com.example.bikerentalsystem.controller;

import com.example.bikerentalsystem.service.BikeService;
import com.example.bikerentalsystem.service.FeedbackService;
import com.example.bikerentalsystem.service.RentalRequestService;
import com.example.bikerentalsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.ArrayList;

@Controller
public class AdminController {

    @Autowired
    private BikeService bikeService;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private RentalRequestService rentalRequestService;

    @Autowired
    private UserService userService;

    @GetMapping("/admin")
    public String showAdminDashboard() {
        return "admin-dashboard";
    }

    @GetMapping("/admin/bikes")
    public String viewBikes(Model model) {
        model.addAttribute("bikes", bikeService.getAllBikes("src/main/resources/data/bikes.txt"));
        return "bikelist";
    }

    @GetMapping("/admin/rentals")
    public String viewRentalRequests(Model model) {
        model.addAttribute("rentalRequests", rentalRequestService.getAllRequests());
        return "requestlist";
    }

    @GetMapping("/admin/feedbacks")
    public String viewFeedbacks(Model model) {
        model.addAttribute("feedbacks", feedbackService.getAllFeedbacks());
        return "feedbacklist";
    }
    @GetMapping("/admin/users")
    public String viewUsers(Model model) {
        try {
            model.addAttribute("users", userService.getAllUsers());
        } catch (IOException e) {
            model.addAttribute("error", "Unable to load users.");
            model.addAttribute("users", new ArrayList<>()); // avoid null on error
        }
        return "users";
    }
}