package com.example.demo.controller;

import com.example.entity.Order;
import com.example.entity.OrderDetail;
import com.example.entity.Product;
import com.example.repository.OrderRepository;
import com.example.repository.ProductRepository;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderHistoryController {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderHistoryController(
            OrderRepository orderRepository,
            ProductRepository productRepository) {

        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @GetMapping
    public String showOrderHistory(
            Authentication authentication,
            Model model) {

        String username = authentication.getName();

        List<Order> orders =
                orderRepository
                        .findByUserUsernameOrderByOrderDateDesc(
                                username);

        DateTimeFormatter dateFormatter =
                DateTimeFormatter.ofPattern(
                        "dd/MM/yyyy HH:mm");

        model.addAttribute("orders", orders);
        model.addAttribute(
                "dateFormatter", dateFormatter);

        return "user/order-history";
    }

    @PostMapping("/{orderId}/cancel")
    @Transactional
    public String cancelOrder(
            @PathVariable("orderId") Long orderId,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        String username = authentication.getName();

        Order order = orderRepository
                .findByOrderIdAndUserUsername(
                        orderId, username)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Không tìm thấy đơn hàng"));

        if (!"PENDING".equals(order.getStatus())) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "Đơn hàng này không thể hủy");

            return "redirect:/orders";
        }

        // Hoàn lại số lượng sản phẩm
        for (OrderDetail detail :
                order.getOrderDetails()) {

            Product product = detail.getProduct();

            if (product != null) {
                int currentStock =
                        product.getStock() == null
                                ? 0
                                : product.getStock();

                product.setStock(
                        currentStock
                                + detail.getQuantity());

                productRepository.save(product);
            }
        }

        order.setStatus("CANCELLED");
        orderRepository.save(order);

        redirectAttributes.addFlashAttribute(
                "success",
                "Hủy đơn hàng #" + orderId
                        + " thành công");

        return "redirect:/orders";
    }
}