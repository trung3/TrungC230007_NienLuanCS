package com.example.demo.controller;

import com.example.entity.CartItem;
import com.example.entity.Order;
import com.example.entity.OrderDetail;
import com.example.entity.Product;
import com.example.entity.User;
import com.example.repository.OrderRepository;
import com.example.repository.ProductRepository;
import com.example.repository.UserRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CheckoutController(
            OrderRepository orderRepository,
            ProductRepository productRepository,
            UserRepository userRepository) {

        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @SuppressWarnings("unchecked")
    private Map<Long, CartItem> getCart(HttpSession session) {
        Map<Long, CartItem> cart =
                (Map<Long, CartItem>) session.getAttribute(
                        CartController.CART_SESSION_KEY);

        if (cart == null) {
            cart = new LinkedHashMap<>();
        }

        return cart;
    }

    private User getCurrentUser(Authentication authentication) {
        User user = userRepository.findByUsername(
                authentication.getName());

        if (user == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Không tìm thấy tài khoản");
        }

        return user;
    }

    @GetMapping
    public String showCheckout(
            HttpSession session,
            Authentication authentication,
            Model model) {

        Map<Long, CartItem> cart = getCart(session);

        if (cart.isEmpty()) {
            return "redirect:/cart";
        }

        double total = cart.values()
                .stream()
                .mapToDouble(CartItem::getSubtotal)
                .sum();

        User user = getCurrentUser(authentication);

        model.addAttribute("cartItems", cart.values());
        model.addAttribute("total", total);
        model.addAttribute("user", user);

        return "user/checkout";
    }

    @PostMapping("/place")
    @Transactional
    public String placeOrder(
            @RequestParam String fullName,
            @RequestParam String phone,
            @RequestParam String address,
            HttpSession session,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        Map<Long, CartItem> cart = getCart(session);

        if (cart.isEmpty()) {
            redirectAttributes.addFlashAttribute(
                    "error", "Giỏ hàng đang trống");

            return "redirect:/cart";
        }

        if (fullName.isBlank()
                || phone.isBlank()
                || address.isBlank()) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Vui lòng nhập đầy đủ thông tin giao hàng");

            return "redirect:/checkout";
        }

        Map<Long, Product> products = new LinkedHashMap<>();

        // Kiểm tra sản phẩm và tồn kho trước
        for (CartItem item : cart.values()) {
            Product product = productRepository
                    .findById(item.getProductId())
                    .orElse(null);

            if (product == null) {
                redirectAttributes.addFlashAttribute(
                        "error",
                        "Sản phẩm " + item.getProductName()
                                + " không còn tồn tại");

                return "redirect:/checkout";
            }

            if (product.getStock() == null
                    || product.getStock() < item.getQuantity()) {

                redirectAttributes.addFlashAttribute(
                        "error",
                        "Sản phẩm " + product.getProductName()
                                + " không đủ số lượng");

                return "redirect:/checkout";
            }

            products.put(product.getProductId(), product);
        }

        User user = getCurrentUser(authentication);

        // Cập nhật thông tin giao hàng của người dùng
        user.setFullName(fullName.trim());
        user.setPhone(phone.trim());
        user.setAddress(address.trim());
        userRepository.save(user);

        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");
        order.setUser(user);

        double totalAmount = 0;

        for (CartItem item : cart.values()) {
            Product product = products.get(item.getProductId());

            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(product);
            detail.setQuantity(item.getQuantity());
            detail.setPrice(product.getPrice());

            order.getOrderDetails().add(detail);

            totalAmount += product.getPrice()
                    * item.getQuantity();

            product.setStock(
                    product.getStock() - item.getQuantity());

            productRepository.save(product);
        }

        order.setTotalAmount(totalAmount);
        Order savedOrder = orderRepository.save(order);

        session.removeAttribute(CartController.CART_SESSION_KEY);

        return "redirect:/checkout/success/"
                + savedOrder.getOrderId();
    }

    @GetMapping("/success/{orderId}")
    public String orderSuccess(
            @PathVariable("orderId") Long orderId,
            Authentication authentication,
            Model model) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Không tìm thấy đơn hàng"));

        if (!order.getUser().getUsername()
                .equals(authentication.getName())) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Bạn không được xem đơn hàng này");
        }

        model.addAttribute("order", order);

        return "user/order-success";
    }
}