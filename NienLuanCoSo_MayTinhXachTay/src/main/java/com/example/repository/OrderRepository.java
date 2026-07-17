package com.example.repository;

import com.example.entity.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // 1. Câu query biểu đồ doanh thu theo ngày (đã làm ở bước trước)
    @Query(value = "SELECT DATE(order_date) as date, SUM(total_amount) as revenue " +
                   "FROM orders WHERE status = 'DELIVERED' " +
                   "GROUP BY DATE(order_date) " +
                   "ORDER BY DATE(order_date) DESC LIMIT 7", nativeQuery = true)
    List<Object[]> getRevenueLast7Days();

    // 2. Lấy danh sách đơn hàng mới nhất (Sắp xếp theo ngày giảm dần)
    @Query("SELECT o FROM Order o ORDER BY o.orderDate DESC")
    List<Order> findTop5RecentOrders(Pageable pageable);
    
    @EntityGraph(attributePaths = {
            "orderDetails",
            "orderDetails.product"
    })
    List<Order> findByUserUsernameOrderByOrderDateDesc(
            String username);

    @EntityGraph(attributePaths = {
            "orderDetails",
            "orderDetails.product"
    })
    Optional<Order> findByOrderIdAndUserUsername(
            Long orderId,
            String username);
}