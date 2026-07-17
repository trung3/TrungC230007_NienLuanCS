package com.example.demo.controller;

import com.example.entity.CartItem;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Map;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("cartCount")
    @SuppressWarnings("unchecked")
    public int cartCount(HttpSession session) {
        Map<Long, CartItem> cart =
                (Map<Long, CartItem>) session.getAttribute(
                        CartController.CART_SESSION_KEY);

        if (cart == null) {
            return 0;
        }

        return cart.values()
                .stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
}