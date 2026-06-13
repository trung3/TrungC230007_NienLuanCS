package com.example.demo.servece;

import java.util.List;

import com.example.entity.Product;

public interface ProductService {

    List<Product> findAll();

    Product findById(Long id);

    Product save(Product product);

    void delete(Long id);
    List<Product> search(String keyword);

    List<Product> findByCategory(Long categoryId);

    List<Product> findByBrand(Long brandId);
}