package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.servece.CategoryService;
import com.example.entity.Category;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // 1. Hiển thị danh sách
    @GetMapping
    public String list(Model model) {
        model.addAttribute("categorys", categoryService.findAll()); // Khớp với th:each="c : ${categorys}"
        model.addAttribute("newcategory", new Category());         // Khớp với th:object="${newcategory}"
        return "admin/category";
    }

    // 2. Lưu (Thêm mới và Sửa)
    @PostMapping("/save")
    public String save(@ModelAttribute("newcategory") Category category) {
        categoryService.save(category);
        return "redirect:/admin/categories"; // Redirect về đúng đường dẫn @RequestMapping
    }

    // 3. Edit (Lấy dữ liệu đổ lên form)
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("categorys", categoryService.findAll());
        model.addAttribute("newcategory", categoryService.findById(id)); // Đổ vào biến newcategory
        return "admin/category";
    }

    // 4. Xóa
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        categoryService.delete(id);
        return "redirect:/admin/categories";
    }
}