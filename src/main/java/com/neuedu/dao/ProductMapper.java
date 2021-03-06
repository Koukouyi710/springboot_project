package com.neuedu.dao;

import com.neuedu.pojo.Category;
import com.neuedu.pojo.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;

@Mapper
public interface ProductMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_product
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(@Param("id") Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_product
     *
     * @mbggenerated
     */
    int insert(@Param("product") Product record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_product
     *
     * @mbggenerated
     */
    Product selectByPrimaryKey(@Param("id") Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_product
     *
     * @mbggenerated
     */
    List<Product> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_product
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(@Param("product") Product record);

    //上架商品数量
    int selectCountup();
    //未上架商品数量
    int selectCountdown();
    //分页
    List<Product> findByPageup(HashMap<String, Object> map);

    //分页
    List<Product> findByPagedown(HashMap<String, Object> map);

    /**
     * 按照productId/Name模糊查询
     */
    List<Product> findProductByProductIdAndProductName(@Param("productId") Integer productId,
                                                       @Param("productName") String productName);


    /**
     * 搜索
     */
    List<Product> searchProduct(@Param("integerSet") Set<Integer> integerSet,
                                @Param("keyword") String keyword);


    /**
     * 获取商品分类
     */
    List<Product> selectBySId(@Param("sid") Integer sid);

    /**
     * 按照productId/Name模糊查询
     */
    List<Product> selectByMark(@Param("isNew") Integer isNew,
                               @Param("isHot") Integer isHot,
                               @Param("isBannner") Integer isBannner);

}