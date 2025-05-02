package com.zwt.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderId; // 与前端 "ORD001" 匹配
    private String game;
    private String service;
    private String status;
    private BigDecimal price;
    private LocalDateTime createTime;
    private String screenshot; // 截图的 URL (例如，存储在云存储中)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // 与客户的关系

    @ManyToOne
    @JoinColumn(name = "product_id") // 订单直接关联一个商品
    @JsonBackReference("defaultReference") // 添加此注解
    private Product product;

    // 评价信息
    private String reviewUser; // 评价用户
    private Integer reviewRating; // 评分
    private String reviewContent; // 评价内容
    private LocalDateTime reviewDate; // 评价日期

}
