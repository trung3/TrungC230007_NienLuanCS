package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.servece.UserService;
import com.example.entity.Role;
import com.example.entity.User;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;

@Controller
public class taikhoanController {

	@Autowired
    private UserService userService;
	@Autowired
	private  UserRepository userRepository;
	@Autowired
    private  RoleRepository roleRepository;
	@Autowired
    private  BCryptPasswordEncoder passwordEncoder;
	@GetMapping("/login")
	public String login() {
		return "login/login";
	}
	 @PostMapping("/login")
	    public String login(@RequestParam String username,
	                        @RequestParam String password,
	                        Model model) {

	        boolean success = userService.login(username, password);

	        if (success) {
	            return "redirect:/index";
	        }

	        model.addAttribute("error", "Sai tài khoản hoặc mật khẩu!");
	        return "login/login";
	    }
	 @GetMapping("/dk")
	 public String dk() {
			return "login/dk";
		}
	 @PostMapping("/register")
	    public String register(
	            @RequestParam String fullName,
	            @RequestParam String username,
	            @RequestParam String email,
	            @RequestParam(defaultValue = "") String phone,
	            @RequestParam(defaultValue = "") String address,
	            @RequestParam String password,
	            @RequestParam String confirmPassword,
	            Model model,
	            RedirectAttributes redirectAttributes) {

	        if (!password.equals(confirmPassword)) {
	            setOldValues(
	                    model, fullName, username,
	                    email, phone, address);

	            model.addAttribute(
	                    "error",
	                    "Mật khẩu xác nhận không khớp");

	            return "login/dk";
	        }

	        if (password.length() < 6) {
	            setOldValues(
	                    model, fullName, username,
	                    email, phone, address);

	            model.addAttribute(
	                    "error",
	                    "Mật khẩu phải có ít nhất 6 ký tự");

	            return "login/dk";
	        }

	        if (userRepository.findByUsername(username) != null) {
	            setOldValues(
	                    model, fullName, username,
	                    email, phone, address);

	            model.addAttribute(
	                    "error",
	                    "Tên đăng nhập đã tồn tại");

	            return "login/dk";
	        }

	        // Trong dự án của bạn:
	        // role ID 1 = ADMIN, role ID 2 = USER.
	        Role userRole = roleRepository
	                .findById(2L)
	                .orElse(null);

	        if (userRole == null) {
	            setOldValues(
	                    model, fullName, username,
	                    email, phone, address);

	            model.addAttribute(
	                    "error",
	                    "Database chưa có quyền USER");

	            return "login/dk";
	        }

	        User user = new User();
	        user.setFullName(fullName.trim());
	        user.setUsername(username.trim());
	        user.setEmail(email.trim());
	        user.setPhone(phone.trim());
	        user.setAddress(address.trim());

	        user.setPassword(
	                passwordEncoder.encode(password));

	        user.setRole(userRole);

	        userRepository.save(user);

	        redirectAttributes.addFlashAttribute(
	                "success",
	                "Đăng ký thành công! Hãy đăng nhập.");

	        return "redirect:/login";
	    }

	    private void setOldValues(
	            Model model,
	            String fullName,
	            String username,
	            String email,
	            String phone,
	            String address) {

	        model.addAttribute("fullName", fullName);
	        model.addAttribute("username", username);
	        model.addAttribute("email", email);
	        model.addAttribute("phone", phone);
	        model.addAttribute("address", address);
	    }
}
