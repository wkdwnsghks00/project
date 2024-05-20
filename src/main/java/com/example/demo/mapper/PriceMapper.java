package com.example.demo.mapper;

import com.example.demo.entity.Price;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PriceMapper { /* 제품 ID로 가격 변동 내역 조회 */
    @Select("SELECT * FROM price WHERE product_id = #{productId}")
    List<Price> findPricesByProductId(int productId);

    @Insert("INSERT INTO price (origin_price, sale_price, coupon_price, discount_rate, crawl_time, product_id) " +
        "VALUES (#{originPrice}, #{salePrice}, #{couponPrice}, #{discountRate}, #{crawlTime}, #{productId})")
    void insertPrice(Price price); /* 새로운 가격 정보 추가 */

    @Update("UPDATE price SET origin_price=#{originPrice}, sale_price=#{salePrice}, coupon_price=#{couponPrice}, " +
        "discount_rate=#{discountRate}, crawl_time=#{crawlTime} WHERE id=#{id}")
    void updatePrice(Price price); /* 기존 가격 정보 업데이트 */

    @Delete("DELETE FROM price WHERE id = #{id}")
    void deletePrice(int id); /* 기존 가격 정보 삭제 */

}

