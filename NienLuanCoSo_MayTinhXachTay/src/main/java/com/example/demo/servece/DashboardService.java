package com.example.demo.servece;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.entity.Order;
import com.example.repository.BrandRepository;
import com.example.repository.CategoryRepository;
import com.example.repository.OrderRepository;
import com.example.repository.ProductRepository;

@Service
public class DashboardService {
	@Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private OrderRepository orderRepository;
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap();

        // Lấy 3 chỉ số số lượng theo đúng yêu cầu đề tài
        stats.put("totalProducts", productRepository.countTotalProducts());
        stats.put("totalCategories", categoryRepository.countTotalCategories());
        stats.put("totalBrands", brandRepository.countTotalBrands());

        // Xử lý dữ liệu biểu đồ: Số lượng laptop theo từng Hãng (Brand)
        List<Object[]> brandChartData = productRepository.countProductsByBrand();
        List<String> brandLabels = new ArrayList();
        List<Long> brandCounts = new ArrayList();

        for (Object[] row : brandChartData) {
            brandLabels.add(row[0] != null ? row[0].toString() : "Chưa phân loại");
            brandCounts.add(Long.parseLong(row[1].toString()));
        }

        stats.put("brandLabels", brandLabels);
        stats.put("brandCounts", brandCounts);

        return stats;
    }
    public Map<String, Object> getAdvancedDashboardData() {
        Map<String, Object> data = new HashMap<>();

        // 1. Lấy dữ liệu 7 ngày gần nhất cho biểu đồ doanh thu
        List<Object[]> rawChartData = orderRepository.getRevenueLast7Days();
        List<String> revenueLabels = new ArrayList<>();
        List<Double> revenueData = new ArrayList<>();
        Collections.reverse(rawChartData); // Đảo ngược chuỗi để chạy từ quá khứ đến hiện tại
        for (Object[] row : rawChartData) {
            revenueLabels.add(row[0].toString());
            revenueData.add(Double.parseDouble(row[1].toString()));
        }
        data.put("revenueLabels", revenueLabels);
        data.put("revenueData", revenueData);

        // 2. Lấy 5 đơn hàng gần nhất từ Database thật
        List<Order> recentOrders = orderRepository.findTop5RecentOrders(PageRequest.of(0, 5));
        data.put("recentOrders", recentOrders);

        return data;
    }
}
