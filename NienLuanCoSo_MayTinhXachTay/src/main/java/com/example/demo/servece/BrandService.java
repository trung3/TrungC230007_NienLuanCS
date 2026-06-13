package com.example.demo.servece;

import java.util.List;

import com.example.entity.Brand;



public interface BrandService {

    List<Brand> findAll();

    Brand findById(Long id);

    Brand save(Brand brand);
  
    void delete(Long id);
}