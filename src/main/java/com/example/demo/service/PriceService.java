package com.example.demo.service;

import com.example.demo.entity.Price;
import com.example.demo.mapper.PriceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriceService {
    @Autowired
    private PriceMapper priceMapper;

    public List<Price> getPricesByProductId(int productId) { /* 제품 ID로 가격 변동 내역 조회 */
        return priceMapper.findPricesByProductId(productId);
    }

    public void addPrice(Price price) { /* 새로운 가격 정보 추가 */
        priceMapper.insertPrice(price);
    }

    public void updatePrice(int id, Price price) { /* 기존 가격 정보 업데이트 */
        price.setId(id);
        priceMapper.updatePrice(price);
    }

    public void deletePrice(int id) { /* 기존 가격 정보 삭제 */
        priceMapper.deletePrice(id);
    }

}

