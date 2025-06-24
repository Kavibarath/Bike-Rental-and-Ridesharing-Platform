package com.example.bikerentalsystem.controller;

import com.example.bikerentalsystem.model.RentalRequest;
import com.example.bikerentalsystem.service.RentalRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/requests")
public class RentalRequestController {
    @Autowired
    private RentalRequestService rentalRequestService;

    @GetMapping
    public String listRequests(Model model) {
        model.addAttribute("requests", rentalRequestService.getAllRequests());
        return "requestlist";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("request", new RentalRequest());
        return "requestadd";
    }

    @PostMapping("/add")
    public String addRequest(@RequestParam String userName, @RequestParam String bikeType, Model model) {
        RentalRequest request = new RentalRequest();
        request.setUserName(userName);
        request.setBikeType(bikeType);
        rentalRequestService.addRequest(request);

        model.addAttribute("message", request.getStatus().equals("Assigned") ?
                "Bike assigned successfully!" : "Request added to queue. You'll be notified when a bike is available.");
        model.addAttribute("requests", rentalRequestService.getAllRequests());
        return "requestlist";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        RentalRequest request = rentalRequestService.getAllRequests().stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid ID"));
        model.addAttribute("request", request);
        return "requestedit";
    }

    @PostMapping("/edit")
    public String updateRequest(@RequestParam Long id, @RequestParam String userName, @RequestParam String bikeType, @RequestParam String status, Model model) {
        RentalRequest updatedRequest = new RentalRequest();
        updatedRequest.setId(id);
        updatedRequest.setUserName(userName);
        updatedRequest.setBikeType(bikeType);
        updatedRequest.setStatus(status);
        rentalRequestService.updateRequest(updatedRequest);

        model.addAttribute("message", "Request updated successfully!");
        model.addAttribute("requests", rentalRequestService.getAllRequests()); // Refresh with latest data
        return "redirect:/requests"; // Navigate to list page
    }

    @GetMapping("/delete/{id}")
    public String showDeleteForm(@PathVariable Long id, Model model) {
        RentalRequest request = rentalRequestService.getAllRequests().stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid ID"));
        model.addAttribute("request", request);
        return "requestdelete";
    }

    @PostMapping("/delete/{id}")
    public String deleteRequest(@PathVariable Long id, Model model) {
        rentalRequestService.deleteRequest(id);
        model.addAttribute("message", "Request deleted successfully!");
        model.addAttribute("requests", rentalRequestService.getAllRequests());
        return "redirect:/requests"; // Navigate to list page
    }
}