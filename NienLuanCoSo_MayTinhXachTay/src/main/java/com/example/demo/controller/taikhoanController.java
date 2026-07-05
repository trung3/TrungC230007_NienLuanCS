package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.servece.UserService;

@Controller
public class taikhoanController {

	@Autowired
    private UserService userService;

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
	            return "redirect:/user";
	        }

	        model.addAttribute("error", "Sai tài khoản hoặc mật khẩu!");
	        return "login/login";
	    }
	 @GetMapping("/dk")
	 public String dk() {
			return "login/dk";
		}
	
}
