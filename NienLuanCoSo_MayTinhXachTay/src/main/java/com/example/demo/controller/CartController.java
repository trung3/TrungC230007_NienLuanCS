package com.example.demo.controller;


import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.example.entity.CartItem;
import com.example.entity.Product;
import com.example.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {

    public static final String CART_SESSION_KEY = "CART";

    private final ProductRepository productRepository;

    public CartController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @SuppressWarnings("unchecked")
    private Map<Long, CartItem> getCart(HttpSession session) {
        Map<Long, CartItem> cart =
                (Map<Long, CartItem>) session.getAttribute(CART_SESSION_KEY);

        if (cart == null) {
            cart = new LinkedHashMap<>();
            session.setAttribute(CART_SESSION_KEY, cart);
        }

        return cart;
    }

    @PostMapping("/add/{productId}")
    public String addToCart(
            @PathVariable Long productId,
            HttpSession session) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Không tìm thấy sản phẩm"));

        Map<Long, CartItem> cart = getCart(session);
        CartItem item = cart.get(productId);

        if (item == null) {
            item = new CartItem(
                    product.getProductId(),
                    product.getProductName(),
                    product.getPrice(),
                    product.getImage(),
                    1
            );

            cart.put(productId, item);
        } else {
            item.setQuantity(item.getQuantity() + 1);
        }

        int cartCount = cart.values()
                .stream()
                .mapToInt(CartItem::getQuantity)
                .sum();

        return "redirect:/cart";
    }

    @GetMapping
    public String showCart(HttpSession session, Model model) {
        Map<Long, CartItem> cart = getCart(session);

        double total = cart.values()
                .stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        model.addAttribute("cartItems", cart.values());
        model.addAttribute("total", total);

        return "user/cart";
    }

    @PostMapping("/update/{productId}")
    public String updateQuantity(
            @PathVariable Long productId,
            @RequestParam int quantity,
            HttpSession session) {

        Map<Long, CartItem> cart = getCart(session);
        CartItem item = cart.get(productId);

        if (item != null) {
            if (quantity <= 0) {
                cart.remove(productId);
            } else {
                item.setQuantity(quantity);
            }
        }

        return "redirect:/cart";
    }

    @PostMapping("/remove/{productId}")
    public String removeItem(
            @PathVariable Long productId,
            HttpSession session) {

        getCart(session).remove(productId);
        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart(HttpSession session) {
        session.removeAttribute(CART_SESSION_KEY);
        return "redirect:/cart";
    }
}