package com.example.demo.controller;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.servece.BrandService;
import com.example.demo.servece.CategoryService;
import com.example.demo.servece.ProductImageService;
import com.example.demo.servece.ProductService;
import com.example.entity.Product;
import com.example.entity.ProductImage;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/products")
public class ProductController {

    @Autowired private ProductService productService;
    @Autowired private ProductImageService productImageService;
    @Autowired private  BrandService brandService;
    @Autowired private  CategoryService categoryService;
    @GetMapping("") // URL sẽ là /admin/products/
    public String listProducts(Model model) {
    	 model.addAttribute("products", productService.findAll());
         model.addAttribute("newProduct", new Product());
         
         // Cần truyền thêm 2 danh sách này để đổ vào thẻ <select>
         model.addAttribute("brands", brandService.findAll());
         model.addAttribute("categories", categoryService.findAll());
       
        return "admin/product"; // Tên file HTML nằm trong thư mục templates/admin/product.html
    }
    @PostMapping("/save")
    public String save(@ModelAttribute("newProduct") Product product) {
        productService.save(product);
        return "redirect:/admin/products";
    }
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("products", productService.findAll());
        model.addAttribute("newProduct", productService.findById(id));
        
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        return "admin/product";
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        productService.delete(id);
        return "redirect:/admin/products";
    }
    // 1. Hiển thị trang quản lý ảnh
    @GetMapping("/images/{productId}")
    public String manageImages(@PathVariable Long productId, Model model) {
        Product product = productService.findById(productId);
        model.addAttribute("product", product);
        return "admin/product-images";
    }
    @GetMapping("/images/set-thumbnail/{productId}/{imageId}")
    public String setThumbnail(@PathVariable Long productId, @PathVariable Long imageId) {
        // Lấy tên file từ ảnh đã chọn
        ProductImage img = productImageService.findById(imageId); 
        
        // Gọi hàm Service vừa tạo
        productService.updateThumbnail(productId, img.getImageUrl());
        
        return "redirect:/admin/products/images/" + productId;
    }
    @PostMapping("/images/add/{productId}")
    public String addImages(@PathVariable Long productId, 
                            @RequestParam("imageFiles") MultipartFile[] files) {
        
        // Lấy đường dẫn gốc của project
        String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/images/";
        
        File uploadDirFile = new File(uploadDir);
        if (!uploadDirFile.exists()) uploadDirFile.mkdirs();

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                try {
                    // Lưu vào thư mục trong project
                    file.transferTo(new File(uploadDir + fileName));
                    
                    // Lưu tên file vào database
                    ProductImage img = new ProductImage();
                    img.setImageUrl(fileName);
                    img.setProduct(productService.findById(productId));
                    productImageService.save(img);
                } catch (Exception e) { e.printStackTrace(); }
            }
        }
        return "redirect:/admin/products/images/" + productId;
    }

    // 3. Xóa ảnh
    @GetMapping("/images/delete/{id}")
    public String deleteImage(@PathVariable Long id, @RequestParam Long productId) {
        productImageService.delete(id);
        return "redirect:/admin/products/images/" + productId;
    }
}