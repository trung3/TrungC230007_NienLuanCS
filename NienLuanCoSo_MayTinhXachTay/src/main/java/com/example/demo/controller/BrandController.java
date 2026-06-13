package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.servece.BrandService;
import com.example.entity.Brand;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @GetMapping
    public List<Brand> getAll() {
        return brandService.findAll();
    }

    @PostMapping
    public Brand create(
            @RequestBody Brand brand) {
        return brandService.save(brand);
    }

    @PutMapping("/{id}")
    public Brand update(
            @PathVariable Long id,
            @RequestBody Brand brand) {

        brand.setBrandId(id);
        return brandService.save(brand);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
    	brandService.delete(id);
    }
}