package com.company.employeetracker.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails != null) {
            model.addAttribute("username", userDetails.getUsername());
            model.addAttribute("role", userDetails.getAuthorities().toString());
        }
        model.addAttribute("activeTab", "dashboard");
        return "dashboard";
    }

    @GetMapping("/employees")
    public String employees(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails != null) {
            model.addAttribute("username", userDetails.getUsername());
            model.addAttribute("role", userDetails.getAuthorities().toString());
        }
        model.addAttribute("activeTab", "employees");
        return "employees";
    }

    @GetMapping("/departments")
    public String departments(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails != null) {
            model.addAttribute("username", userDetails.getUsername());
            model.addAttribute("role", userDetails.getAuthorities().toString());
        }
        model.addAttribute("activeTab", "departments");
        return "departments";
    }

    @GetMapping("/reviews")
    public String reviews(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails != null) {
            model.addAttribute("username", userDetails.getUsername());
            model.addAttribute("role", userDetails.getAuthorities().toString());
        }
        model.addAttribute("activeTab", "reviews");
        return "reviews";
    }

    @GetMapping("/goals")
    public String goals(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails != null) {
            model.addAttribute("username", userDetails.getUsername());
            model.addAttribute("role", userDetails.getAuthorities().toString());
        }
        model.addAttribute("activeTab", "goals");
        return "goals";
    }

    @GetMapping("/attendance")
    public String attendance(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails != null) {
            model.addAttribute("username", userDetails.getUsername());
            model.addAttribute("role", userDetails.getAuthorities().toString());
        }
        model.addAttribute("activeTab", "attendance");
        return "attendance";
    }

    @GetMapping("/reports")
    public String reports(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails != null) {
            model.addAttribute("username", userDetails.getUsername());
            model.addAttribute("role", userDetails.getAuthorities().toString());
        }
        model.addAttribute("activeTab", "reports");
        return "reports";
    }
}
