package com.zwt;


import com.zwt.model.Product;
//import com.zwt.model.Review;
import com.zwt.repo.ProductRepository;
//import com.zwt.repo.ReviewRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class DataSeeder {
    @Bean
    public CommandLineRunner seedData(ProductRepository productRepository) {
        return args -> {
            if (productRepository.count() == 0) {
                // 创建示例产品
                Product product1 = new Product();
                product1.setTitle("DLC 4");
                product1.setImage("https://nocode.meituan.com/photo/search?keyword=game,dlc&width=400&height=300");
                product1.setDescription("游戏DLC内容");
                product1.setPrice(new BigDecimal("0.001"));
                product1.setStatus("库存充足");
                product1.setSales(156);

                // 先保存产品
                product1 = productRepository.save(product1);

//                // 创建评论并设置关联
//                Review review1 = new Review();
//                review1.setContent("很好的游戏内容");
//                review1.setRating(5);
//                review1.setDate(LocalDateTime.now());
//                review1.setUsername("user1");
//                review1.setProduct(product1);
//
//                Review review2 = new Review();
//                review2.setContent("不错的DLC");
//                review2.setRating(4);
//                review2.setDate(LocalDateTime.now());
//                review2.setUsername("user2");
//                review2.setProduct(product1);

//                // 保存评论
//                reviewRepository.saveAll(List.of(review1, review2));

                // product2 部分保持不变
                Product product2 = new Product();
                product2.setTitle("40 LEVEL");
                product2.setImage("https://nocode.meituan.com/photo/search?keyword=game,level&width=400&height=300");
                product2.setDescription("等级提升服务");
                product2.setPrice(new BigDecimal("0.002"));
                product2.setStatus("库存充足");
                product2.setSales(89);
                productRepository.save(product2);
            }
        };
    }
}


