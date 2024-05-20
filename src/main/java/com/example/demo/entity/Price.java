package com.example.demo.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Price {
    private int id;
    private int originPrice;
    private int salePrice;
    private int couponPrice;
    private String discountRate;
    private LocalDateTime crawlTime;
    private int productId;
}

