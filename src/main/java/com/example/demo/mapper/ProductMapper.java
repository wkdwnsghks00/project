package com.example.demo.mapper;
import com.example.demo.entity.Product;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import org.apache.ibatis.annotations.Update;


@Mapper
public interface ProductMapper {
    @Select("SELECT * FROM product WHERE id = #{id}")
    Product findProductById(int id);

    @Select("SELECT * FROM product WHERE category_id = #{categoryId}")
    List<Product> findProductsByCategoryId(int categoryId);

    @Insert("INSERT INTO product (title, brand, prod_option, description, img_url, url, category_id) " +
        "VALUES (#{title}, #{brand}, #{option}, #{description}, #{imgUrl}, #{url}, #{categoryId})")
    void insertProduct(Product product);

    @Update("UPDATE product SET title=#{title}, brand=#{brand}, prod_option=#{option}, description=#{description}, " +
        "img_url=#{imgUrl}, url=#{url}, category_id=#{categoryId} WHERE id=#{id}")
    void updateProduct(Product product);

    @Delete("DELETE FROM product WHERE id = #{id}")
    void deleteProduct(int id);

    @Select("SELECT * FROM product WHERE title LIKE CONCAT('%', #{keyword}, '%') OR description LIKE CONCAT('%', #{keyword}, '%')")
    List<Product> searchProducts(String keyword); /* 제품 검색 기능 */

    @Select("SELECT * FROM product")
    List<Product> findAllProducts(); /* 전체 제품 목록 조회 */

    @Select("SELECT * FROM product p JOIN price pr ON p.id = pr.product_id WHERE pr.discount_rate >= #{discountRate}")  /* 일정 할인율 이상의 할인 상품 조회 */
    List<Product> findDiscountedProducts(@Param("discountRate") int discountRate);
}
