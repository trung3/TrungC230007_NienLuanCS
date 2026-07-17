package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.servece.ProductService;

@Controller
@RequestMapping("/index")
public class userController {
	@Autowired
    private ProductService productService;
	 @GetMapping("")
	public String a(Model m){
		 m.addAttribute("products", productService.findAll());
	        System.err.println(productService.findAll());
		 return "user/index.html";
	}
}
