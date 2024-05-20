package com.example.demo.entity;

import lombok.Data;

@Data
public class Product {
    private int id;
    private String title;
    private String brand;
    private String option;
    private String description;
    private String imgUrl;
    private String url;
    private int categoryId;
}
