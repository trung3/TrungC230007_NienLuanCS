package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.entity.Category;

@Repository
public interface CategoryRepository
        extends JpaRepository<Category, Long> {
	// Thống kê 2: Tổng số danh mục
    @Query("SELECT COUNT(c) FROM Category c")
    Long countTotalCategories();
}