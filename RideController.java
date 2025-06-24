package com.example.bikerentalsystem.controller;
import com.example.bikerentalsystem.model.Ride;
import com.example.bikerentalsystem.service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class RideController {

    @Autowired
    private RideService rideService;

    @GetMapping("/create-ride")
    public String showCreateRideForm(Model model) {
        model.addAttribute("ride", new Ride());
        return "create-ride";
    }
    @PostMapping("/create-ride")
    public String submitRideForm(@ModelAttribute Ride ride, Model model) {
        rideService.saveRide(ride);
        return "redirect:/all-rides"; // 🔁 Redirect to All Rides
    }
    @GetMapping("/all-rides")
    public String viewAllRides(Model model) {
        model.addAttribute("rides", rideService.getAllRides());
        return "all-rides";
    }
    @GetMapping("/edit-ride/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        List<Ride> rides = rideService.getAllRides();
        if (id >= 0 && id < rides.size()) {
            Ride ride = rides.get(id);
            model.addAttribute("ride", ride);
            return "edit-ride";
        }
        return "redirect:/all-rides";
    }
    @PostMapping("/update-ride/{id}")
    public String updateRide(@PathVariable int id, @ModelAttribute Ride ride) {
        rideService.updateRide(id, ride);
        return "redirect:/all-rides";
    }
    @GetMapping("/delete-ride/{id}")
    public String deleteRide(@PathVariable int id) {
        rideService.deleteRide(id);
        return "redirect:/all-rides";
    }
    @GetMapping("/search-rides")
    public String searchRides(@RequestParam("location") String location, Model model) {
        List<Ride> filteredRides = rideService.searchRidesByLocation(location);
        model.addAttribute("rides", filteredRides);
        return "all-rides";
    }
}