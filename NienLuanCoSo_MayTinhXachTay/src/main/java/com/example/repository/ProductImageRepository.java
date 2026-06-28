package com.example.repository;

import com.example.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    // Có thể thêm hàm tìm ảnh theo productId sau này nếu cần
}