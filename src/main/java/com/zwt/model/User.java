package com.zwt.model;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

import java.util.List;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    private boolean emailVerified;
    private BigDecimal balance; // 钱包余额
    private String role; // "user" 或 "admin"

    @OneToMany(mappedBy = "user")
    private List<Order> orders;
}