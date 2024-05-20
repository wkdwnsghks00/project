package com.example.demo.service;

import com.example.demo.entity.BigCategory;
import com.example.demo.mapper.BigCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BigCategoryService {
    @Autowired
    private BigCategoryMapper bigCategoryMapper;

    public List<BigCategory> getAllBigCategories() {
        return bigCategoryMapper.findAll();
    }

    public void addBigCategory(BigCategory bigCategory) {
        bigCategoryMapper.insertBigCategory(bigCategory);
    }

    public void updateBigCategory(int id, BigCategory bigCategory) {
        bigCategory.setId(id);
        bigCategoryMapper.updateBigCategory(bigCategory);
    }

    public void deleteBigCategory(int id) {
        bigCategoryMapper.deleteBigCategory(id);
    }
}
