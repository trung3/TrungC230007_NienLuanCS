package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminPageController {

    @GetMapping("/admin/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/admin/categories")
    public String categories() {
        return "admin/category";
    }

    @GetMapping("/admin/brands")
    public String brands() {
        return "admin/brand";
    }

    @GetMapping("/admin/products")
    public String products() {
        return "admin/product";
    }
}