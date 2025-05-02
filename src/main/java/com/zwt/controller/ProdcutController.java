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

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        log.info("Adding product: {}", product);
        try {
            Product savedProduct = productRepository.save(product);
            log.info("Product added successfully with ID: {}", savedProduct.getId());
            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error adding product", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        log.info("Updating product with ID: {}", id);
        try {
            Optional<Product> productOptional = productRepository.findById(id);
            if (!productOptional.isPresent()) {
                log.warn("Product with ID {} not found", id);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Product product = productOptional.get();
            product.setName(productDetails.getName());
            product.setDescription(productDetails.getDescription());
            product.setPrice(productDetails.getPrice());
            product.setImage(productDetails.getImage());
            product.setStatus(productDetails.getStatus());
            product.setSales(productDetails.getSales());

            Product updatedProduct = productRepository.save(product);
            log.info("Product with ID {} updated successfully", id);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error updating product with ID: {}", id, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}