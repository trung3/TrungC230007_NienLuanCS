package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.servece.DashboardService;
import com.example.entity.Role;
import com.example.entity.User;
import com.example.repository.RoleRepository;

@Controller
@RequestMapping("/admin")
public class AdminPageController {
	@Autowired
	RoleRepository roleRep;
	
	@Autowired
    private DashboardService dashboardService;

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        Map<String, Object> stats = dashboardService.getDashboardStats();

        // Đẩy dữ liệu số lượng ra view
        model.addAttribute("totalProducts", stats.get("totalProducts"));
        model.addAttribute("totalCategories", stats.get("totalCategories"));
        model.addAttribute("totalBrands", stats.get("totalBrands"));

        // Đẩy dữ liệu mảng phục vụ vẽ biểu đồ cơ cấu hãng
        model.addAttribute("brandLabels", stats.get("brandLabels"));
        model.addAttribute("brandCounts", stats.get("brandCounts"));
     // Lấy dữ liệu đếm số lượng của bước trước
        Map<String, Object> basicStats = dashboardService.getDashboardStats();
        model.addAllAttributes(basicStats); 

        // Lấy dữ liệu biểu đồ doanh thu và đơn hàng gần đây mới bổ sung
        Map<String, Object> advancedData = dashboardService.getAdvancedDashboardData();
        model.addAttribute("revenueLabels", advancedData.get("revenueLabels"));
        model.addAttribute("revenueData", advancedData.get("revenueData"));
        model.addAttribute("recentOrders", advancedData.get("recentOrders")); // Danh sách đơn hàng thật
        return "admin/index";
    }
    @GetMapping({"", "/"})
    public String dashboard(Model model) {
        // Gửi thuộc tính sang để Thymeleaf biết menu nào đang active
        model.addAttribute("activePage", "dashboard");
        return "admin/index"; // Trả về file index.html trong templates/admin/
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("activePage", "users");
        User emptyUser = new User();
        emptyUser.setRole(new Role()); // Đảm bảo role không bị null để th:field="*{role.roleId}" không bị lỗi
        List<Role> roles = roleRep.findAll();
        System.out.println("Số lượng vai trò quét được: " + roles.size()); // log kiểm tra nhanh
        model.addAttribute("rolesList", roles);
        model.addAttribute("userForm", emptyUser);
        return "admin/users"; // Trả về file users.html
    }

}