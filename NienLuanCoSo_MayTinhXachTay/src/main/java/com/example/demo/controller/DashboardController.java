package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.repository.BrandRepository;
import com.example.repository.CategoryRepository;
import com.example.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    @GetMapping
    public Map<String, Long> getDashboard() {

    	Map<String, Long> dashboard = new HashMap();

        dashboard.put("products", productRepository.count());
        dashboard.put("categories", categoryRepository.count());
        dashboard.put("brands", brandRepository.count());

        return dashboard;
    }
}
