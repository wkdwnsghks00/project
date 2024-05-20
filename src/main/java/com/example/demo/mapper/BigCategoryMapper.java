package com.example.demo.mapper;

import com.example.demo.entity.BigCategory;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BigCategoryMapper {
    @Select("SELECT * FROM bigcategory")
    List<BigCategory> findAll();

    @Insert("INSERT INTO bigcategory (name) VALUES (#{name})")
    void insertBigCategory(BigCategory bigCategory);

    @Update("UPDATE bigcategory SET name=#{name} WHERE id=#{id}")
    void updateBigCategory(BigCategory bigCategory);

    @Delete("DELETE FROM bigcategory WHERE id = #{id}")
    void deleteBigCategory(int id);
}
