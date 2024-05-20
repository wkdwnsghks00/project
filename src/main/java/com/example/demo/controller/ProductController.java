package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/{id}") /* 제품 ID로 개별 제품 조회 */
    public Product getProductById(@PathVariable int id) {
        return productService.getProductById(id);
    }

    @GetMapping("/category/{categoryId}")  /* 카테고리기반 제품 조회*/
    public List<Product> getProductsByCategoryId(@PathVariable int categoryId) {
        return productService.getProductsByCategoryId(categoryId);
    }

    @PostMapping("/") /* 새로운 제품 db 추가 */
    public void addProduct(@RequestBody Product product) {
        productService.addProduct(product);
    }

    @PutMapping("/{id}") /* 기존 제품 정보 업데이트` */
    public void updateProduct(@PathVariable int id, @RequestBody Product product) {
        productService.updateProduct(id, product);
    }

    @DeleteMapping("/{id}") /* 기존 제품 db 삭제 */
    public void deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
    }

    @GetMapping("/search") /* 제품 검색 */
    public List<Product> searchProducts(@RequestParam String keyword) {
        return productService.searchProducts(keyword);
    }

    @GetMapping("/") /* 모든 제품 조회 */
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/discounted")  /* 일정 할인율 이상의 할인 상품 조회 */
    public List<Product> findDiscountedProducts(@RequestParam int discountRate) {
        return productService.findDiscountedProducts(discountRate);
    }
}
