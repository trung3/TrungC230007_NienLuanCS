package com.example.demo.servece;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Brand;
import com.example.repository.BrandRepository;


@Service
public interface BrandService {
	
    List<Brand> findAll();

    Brand findById(Long id);

    Brand save(Brand brand);
  
    void delete(Long id);
}