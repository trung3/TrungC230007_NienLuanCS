package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.servece.BrandService; // Nhớ kiểm tra lại chữ servece/service cho đúng với project của bạn
import com.example.entity.Brand;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/brands")
@RequiredArgsConstructor // Giữ nguyên Lombok để tự động tiêm BrandService vào giống như bạn viết
public class BrandController {

    private final BrandService brandService;

    // 1. Hiển thị danh sách hãng và truyền Object rỗng vào Form thêm mới
    @GetMapping
    public String listBrands(Model model) {
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("newBrand", new Brand()); // Object rỗng phục vụ Form Thêm mới
        return "admin/brand"; // Tìm đến file src/main/resources/templates/admin/brand.html
    }

    // 2. Xử lý Thêm mới hoặc Cập nhật khi bấm nút "Lưu Hãng"
    @PostMapping("/save")
    public String saveBrand(@ModelAttribute("newBrand") Brand brand) {
        brandService.save(brand);
        return "redirect:/admin/brands"; // Lưu xong quay về trang danh sách để cập nhật bảng
    }

    // 3. Xử lý khi bấm nút "Sửa" (Tìm dữ liệu hãng cũ đổ ngược vào Form bên trái)
    @GetMapping("/edit/{id}")
    public String editBrand(@PathVariable Long id, Model model) {
        model.addAttribute("brands", brandService.findAll()); // Vẫn giữ danh sách hiển thị ở bảng bên phải
        model.addAttribute("newBrand", brandService.findById(id)); // Lấy dữ liệu hãng cũ đè vào Form bên trái
        return "admin/brand"; // Vẫn ở lại trang giao diện brand.html
    }

    // 4. Xử lý khi bấm nút "Xóa"
    @GetMapping("/delete/{id}")
    public String deleteBrand(@PathVariable Long id) {
        brandService.delete(id);
        return "redirect:/admin/brands"; // Xóa xong quay về trang danh sách
    }
}