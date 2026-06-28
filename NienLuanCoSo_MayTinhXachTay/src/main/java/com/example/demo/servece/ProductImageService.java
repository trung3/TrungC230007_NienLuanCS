package com.example.demo.servece;

import com.example.entity.ProductImage;

public interface ProductImageService {
    void save(ProductImage productImage);
    void delete(Long imageId);
    ProductImage findById(Long imageId);
}