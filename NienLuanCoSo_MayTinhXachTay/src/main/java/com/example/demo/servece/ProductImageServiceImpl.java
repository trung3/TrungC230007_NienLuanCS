package com.example.demo.servece;

import com.example.repository.ProductImageRepository;
import com.example.demo.servece.ProductImageService;
import com.example.entity.ProductImage;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductImageServiceImpl implements ProductImageService {

	@Autowired private ProductImageRepository repo;

    @Override
    public void save(ProductImage productImage) { repo.save(productImage); }

    @Override
    public void delete(Long imageId) { repo.deleteById(imageId); }
    @Override
    public ProductImage findById(Long imageId) {
        return repo.findById(imageId).orElseThrow(() -> new RuntimeException("Không tìm thấy ảnh"));
    }
}