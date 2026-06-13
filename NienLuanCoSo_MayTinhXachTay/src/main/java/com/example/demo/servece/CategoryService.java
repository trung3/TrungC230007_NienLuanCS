package com.example.demo.servece;

import java.util.List;

import com.example.entity.Category;
import com.example.repository.CategoryRepository;
import com.example.repository.ProductRepository;

public interface CategoryService {

    List<Category> findAll();

    Category findById(Long id);

    Category save(Category category);
  
    void delete(Long id);
}