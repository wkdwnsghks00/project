package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductMapper productMapper;

    public Product getProductById(int id) { /* 제품 ID로 제품 조회 */
        return productMapper.findProductById(id);
    }

    public List<Product> getProductsByCategoryId(int categoryId) { /* 카테고리기반 제품 조회 */
        return productMapper.findProductsByCategoryId(categoryId);
    }

    public void addProduct(Product product) { /* 새로운 제품 추가 */
        productMapper.insertProduct(product);
    }

    public void updateProduct(int id, Product product) { /* 기존 제품 정보 업데이트 */
        product.setId(id);
        productMapper.updateProduct(product);
    }

    public void deleteProduct(int id) { /* 기존 제품 삭제 */
        productMapper.deleteProduct(id);
    }

    public List<Product> searchProducts(String keyword) { /* 제품 검색 */
        return productMapper.searchProducts(keyword);
    } /* 제품 검색 기능*/

    public List<Product> getAllProducts() { /* 모든 제품 조회 */
        return productMapper.findAllProducts();
    } /* 전체 제품 목록 조회 */

    public List<Product> findDiscountedProducts(int discountRate) {  /* 일정 할인율 이상의 할인 상품 조회 */
        return productMapper.findDiscountedProducts(discountRate);
    }
}