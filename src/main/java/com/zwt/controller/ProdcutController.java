package com.zwt.controller;


import com.zwt.model.Order;
import com.zwt.model.Product;
import com.zwt.repo.OrderRepository;
import com.zwt.repo.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/products")
@Log4j2
public class ProdcutController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<Product>> getProducts() {
        log.info("开始处理 /api/products 请求");
        try {
            List<Product> products = productRepository.findAll();
            log.info("查询到 {} 个产品", products.size());
            // 模拟延迟
            //Thread.sleep(5000);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            log.error("处理 /api/products 请求时发生异常", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // 返回 500 错误
        } finally {
            log.info("结束处理 /api/products 请求");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

}