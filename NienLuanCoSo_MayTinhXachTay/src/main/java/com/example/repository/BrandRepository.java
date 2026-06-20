package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.entity.Brand;


@Repository
public interface BrandRepository
        extends JpaRepository<Brand, Long> {
	// Thống kê 3: Tổng số hãng sản xuất
    @Query("SELECT COUNT(b) FROM Brand b")
    Long countTotalBrands();
}