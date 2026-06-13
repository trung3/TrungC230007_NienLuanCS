package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.servece.ProductService;
import com.example.entity.Product;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<Product> getAll() {
        return productService.findAll();
    }

    @PostMapping
    public Product create(
            @RequestBody Product product) {
        return productService.save(product);
    }

    @PutMapping("/{id}")
    public Product update(
            @PathVariable Long id,
            @RequestBody Product product) {

        product.setProductId(id);

        return productService.save(product);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id) {

        productService.delete(id);
    }
    @GetMapping("/search")
    public List<Product> search(
            @RequestParam String keyword) {

        return productService.search(keyword);
    }
    @GetMapping("/category/{id}")
    public List<Product> findByCategory(
            @PathVariable Long id) {

        return productService.findByCategory(id);
    }
    @GetMapping("/brand/{id}")
    public List<Product> findByBrand(
            @PathVariable Long id) {

        return productService.findByBrand(id);
    }
}
