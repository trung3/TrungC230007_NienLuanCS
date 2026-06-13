package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	List<Product> findByProductNameContaining(String keyword);

	List<Product> findByCategoryCategoryId(Long categoryId);

	List<Product> findByBrandBrandId(Long brandId);
}