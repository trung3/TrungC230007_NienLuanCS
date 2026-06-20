package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	List<Product> findByProductNameContaining(String keyword);

	List<Product> findByCategoryCategoryId(Long categoryId);

	List<Product> findByBrandBrandId(Long brandId);
	// Thống kê 1: Tổng số sản phẩm
    @Query("SELECT COUNT(p) FROM Product p")
    Long countTotalProducts();

    // Thống kê 4: Lấy số lượng sản phẩm theo từng Hãng để vẽ biểu đồ
    @Query("SELECT p.brand.brandName, COUNT(p) FROM Product p GROUP BY p.brand.brandName")
    List<Object[]> countProductsByBrand();
}