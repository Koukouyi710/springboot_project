package com.neuedu.dao;

import com.neuedu.pojo.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CartMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_cart
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(@Param("id")Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_cart
     *
     * @mbggenerated
     */
    int insert(@Param("cart")Cart record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_cart
     *
     * @mbggenerated
     */
    Cart selectByPrimaryKey(@Param("id")Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_cart
     *
     * @mbggenerated
     */
    List<Cart> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_cart
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(@Param("cart")Cart record);

    Cart selectCartByUserIdAndProductId(@Param("userId") Integer userId,@Param("productId") Integer productId);

   List<Cart> selectCartByUserId(@Param("userId") Integer userId);

    /**
     * 是否全选
     * @return >0 未全选
     */
    Integer isCheckedAll(@Param("userId")Integer userId);

}