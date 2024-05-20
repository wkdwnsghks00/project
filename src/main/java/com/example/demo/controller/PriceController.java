package com.example.demo.controller;

import com.example.demo.entity.Price;
import com.example.demo.service.PriceAnalysisService;
import com.example.demo.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prices")
public class PriceController {
    @Autowired
    private PriceService priceService;
    @Autowired
    private PriceAnalysisService priceAnalysisService;


    @GetMapping("/product/{productId}") /* 제품 ID로 가격 변동 내역 조회 */
    public List<Price> getPricesByProductId(@PathVariable int productId) {
        return priceService.getPricesByProductId(productId);
    }

    @PostMapping("/") /* 새로운 가격 정보 추가 */
    public void addPrice(@RequestBody Price price) {
        priceService.addPrice(price);
    }

    @PutMapping("/{id}") /* 기존 가격 정보 업데이트 */
    public void updatePrice(@PathVariable int id, @RequestBody Price price) {
        priceService.updatePrice(id, price);
    }

    @DeleteMapping("/{id}") /* 기존 가격 정보 삭제 */
    public void deletePrice(@PathVariable int id) {
        priceService.deletePrice(id);
    }

    @GetMapping("/trend/{productId}")  /* 특정 제품의 가격 추이 조회 */
    public List<Price> getPriceTrend(@PathVariable int productId) {
        return priceAnalysisService.getPriceTrend(productId);
    }

}
