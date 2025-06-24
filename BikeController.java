package com.example.bikerentalsystem.controller;

import com.example.bikerentalsystem.model.Bike;
import com.example.bikerentalsystem.model.ElectricBike;
import com.example.bikerentalsystem.model.ManualBike;
import com.example.bikerentalsystem.service.BikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/bikes")
public class BikeController {
    @Autowired
    private BikeService bikeService;

    @GetMapping
    public String listBikes(Model model, @RequestParam(required = false) String searchQuery, @RequestParam(required = false) String sortBy) {
        if (searchQuery != null && !searchQuery.isEmpty()) {
            model.addAttribute("bikes", bikeService.searchBikes(searchQuery, sortBy));
        } else {
            model.addAttribute("bikes", bikeService.getAllBikes(sortBy));
        }
        model.addAttribute("searchQuery", searchQuery);
        model.addAttribute("sortBy", sortBy);
        return "bikelist";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("bike", new ElectricBike()); // Default to ElectricBike for form binding
        return "bikeadd";
    }

    @PostMapping("/add")
    public String addBike(@RequestParam String type, @RequestParam String model, @RequestParam int availabilityCount, @RequestParam double rating) {
        Bike bike;
        if (type.equals("Electric")) {
            bike = new ElectricBike();
        } else {
            bike = new ManualBike();
        }
        bike.setModel(model);
        bike.setAvailabilityCount(availabilityCount);
        bike.setRating(rating);
        bikeService.addBike(bike);
        return "redirect:/bikes";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Bike bike = bikeService.getBikeById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID"));
        model.addAttribute("bike", bike);
        return "bikeedit";
    }

    @PostMapping("/edit")
    public String editBike(@RequestParam Long id, @RequestParam String type, @RequestParam String model, @RequestParam int availabilityCount, @RequestParam double rating) {
        Bike bike;
        if (type.equals("Electric")) {
            bike = new ElectricBike(id, model, availabilityCount, rating);
        } else {
            bike = new ManualBike(id, model, availabilityCount, rating);
        }
        bikeService.updateBike(bike);
        return "redirect:/bikes";
    }

    @GetMapping("/delete/{id}")
    public String showDeleteForm(@PathVariable Long id, Model model) {
        Bike bike = bikeService.getBikeById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID"));
        model.addAttribute("bike", bike);
        return "bikedelete";
    }

    @PostMapping("/delete/{id}")
    public String deleteBike(@PathVariable Long id) {
        bikeService.deleteBike(id);
        return "redirect:/bikes";
    }
}