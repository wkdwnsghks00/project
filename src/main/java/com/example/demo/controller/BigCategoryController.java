package com.example.demo.controller;

import com.example.demo.entity.BigCategory;
import com.example.demo.service.BigCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bigcategories")
public class BigCategoryController {
    @Autowired
    private BigCategoryService bigCategoryService;

    @GetMapping("/")
    public List<BigCategory> getAllBigCategories() { /* 모든 대분류 카테고리 조회 */
        return bigCategoryService.getAllBigCategories();
    }

    @PostMapping("/")
    public void addBigCategory(@RequestBody BigCategory bigCategory) {
        bigCategoryService.addBigCategory(bigCategory);
    }

    @PutMapping("/{id}")
    public void updateBigCategory(@PathVariable int id, @RequestBody BigCategory bigCategory) {
        bigCategoryService.updateBigCategory(id, bigCategory);
    }

    @DeleteMapping("/{id}")
    public void deleteBigCategory(@PathVariable int id) {
        bigCategoryService.deleteBigCategory(id);
    }
}
