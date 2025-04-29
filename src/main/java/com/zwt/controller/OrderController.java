package com.zwt.controller;// 导入必要的类

import com.zwt.model.Order;
import com.zwt.model.Product;
import com.zwt.model.User;
import com.zwt.repo.OrderRepository;
import com.zwt.repo.ProductRepository;

import com.zwt.repo.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return ResponseEntity.ok(orders);
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest createOrderRequest) {
        Optional<User> userOptional = userRepository.findById(createOrderRequest.getUserId());
        if (!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        Optional<Product> productOptional = productRepository.findById(createOrderRequest.getProductId());
        if (!productOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }

        Order order = new Order();
        order.setOrderId("ORD" + System.currentTimeMillis()); // 生成订单号
        order.setCreateTime(LocalDateTime.now());
        order.setStatus("待支付"); // 初始状态

        order.setUser(userOptional.get());
        order.setProduct(productOptional.get());
        order = orderRepository.save(order);

        //根据productId获取到的就是orderId
        return ResponseEntity.status(HttpStatus.CREATED).body(String.valueOf(order.getId()));
    }



    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        return orderOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/review")
    public ResponseEntity<?> updateReview(@PathVariable Long id,
                                          @RequestBody ReviewRequest reviewRequest) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (!orderOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
        }

        Order order = orderOptional.get();
        order.setReviewUser(reviewRequest.getUser());
        order.setReviewRating(reviewRequest.getRating());
        order.setReviewContent(reviewRequest.getContent());
        order.setReviewDate(LocalDateTime.now());

        orderRepository.save(order);
        return ResponseEntity.ok("Review updated successfully");
    }

    @PostMapping("/{orderId}/screenshot")
    public ResponseEntity<?> uploadScreenshot(@PathVariable Long orderId,
                                              @RequestParam("file") MultipartFile file) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (!orderOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
        }

        Order order = orderOptional.get();

        try {
            // 1. 保存文件
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            String filePath = "/path/to/your/screenshot/directory/" + fileName; // 替换为你的文件保存路径

            File dest = new File(filePath);
            file.transferTo(dest);

            // 2. 更新订单信息
            order.setScreenshot(filePath); // 保存文件路径
            orderRepository.save(order);

            return ResponseEntity.ok(order); // 返回更新后的订单
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload screenshot");
        }
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (!orderOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
        }

        Order order = orderOptional.get();
        order.setStatus("已取消"); // 或者使用一个枚举类型来表示订单状态
        orderRepository.save(order);

        return ResponseEntity.ok(order); // 返回更新后的订单
    }

    @Data
    static class CreateOrderRequest {
        private Long userId;
        private Long productId;
    }

    @Data
    static class ReviewRequest {
        private String user;
        private Integer rating;
        private String content;
    }
}
