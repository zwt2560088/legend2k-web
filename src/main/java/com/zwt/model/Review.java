//package com.zwt.model;
//
//import com.fasterxml.jackson.annotation.JsonBackReference;
//import jakarta.persistence.*;
//import lombok.Data;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Data
//public class Review {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String username;
//    private Integer rating;
//    private String content;
//    private LocalDateTime date;
//    @ManyToOne
//    @JoinColumn(name = "product_id")
//    @JsonBackReference
//    private Product product;
//}
