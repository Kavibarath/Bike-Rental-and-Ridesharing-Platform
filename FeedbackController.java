
package com.example.bikerentalsystem.controller;

import com.example.bikerentalsystem.model.Feedback;
import com.example.bikerentalsystem.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/feedbacks")
public class FeedbackController {
    @Autowired
    private FeedbackService feedbackService;

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("feedback", new Feedback());
        return "feedbackcreate";
    }

    @PostMapping("/create")
    public String createFeedback(@ModelAttribute Feedback feedback) {
        try {
            feedbackService.createFeedback(feedback);
            return "redirect:/feedbacks";
        } catch (IllegalArgumentException e) {
            return "redirect:/feedbacks/create?error=" + e.getMessage();
        }
    }

    @GetMapping
    public String listFeedbacks(Model model) {
        model.addAttribute("feedbacks", feedbackService.getAllFeedbacks());
        return "feedbacklist";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Feedback feedback = feedbackService.getFeedbackById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID"));
        model.addAttribute("feedback", feedback);
        return "feedbackupdate";
    }

    @PostMapping("/update")
    public String updateFeedback(@ModelAttribute Feedback feedback) {
        try {
            feedbackService.updateFeedback(feedback);
            return "redirect:/feedbacks";
        } catch (IllegalArgumentException e) {
            return "redirect:/feedbacks/update/" + feedback.getId() + "?error=" + e.getMessage();
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteFeedback(@PathVariable Long id) {
        feedbackService.deleteFeedback(id);
        return "redirect:/feedbacks";
    }

    @GetMapping("/")
    public String redirectToFeedbacks() {
        return "redirect:/feedbacks";
    }
}