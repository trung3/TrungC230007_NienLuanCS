package com.example.demo.controller;

import com.example.entity.Product;
import com.example.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class ProductDetailController {

    private final ProductRepository productRepository;

    public ProductDetailController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/product/{id}")
    public String showProductDetail(
            @PathVariable("id") Long id,
            Model model) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Không tìm thấy sản phẩm"
                ));

        model.addAttribute("product", product);

        return "user/product-detail";
    }
}