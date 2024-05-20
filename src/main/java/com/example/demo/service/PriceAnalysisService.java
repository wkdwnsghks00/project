package com.example.demo.service;

import com.example.demo.entity.Price;
import com.example.demo.mapper.PriceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriceAnalysisService {
    @Autowired
    private PriceMapper priceMapper;

    public List<Price> getPriceTrend(int productId) {  /* 특정 제품의 가격 추이 조회 */
        return priceMapper.findPricesByProductId(productId);
    }
}
