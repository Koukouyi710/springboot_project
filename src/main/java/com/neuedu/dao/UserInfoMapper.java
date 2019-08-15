package com.neuedu.dao;

import com.neuedu.pojo.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserInfoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_user
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(@Param("id") Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_user
     *
     * @mbggenerated
     */
    int insert(@Param("user") UserInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_user
     *
     * @mbggenerated
     */
    UserInfo selectByPrimaryKey(@Param("id") Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_user
     *
     * @mbggenerated
     */
    List<UserInfo> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_user
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(@Param("user") UserInfo record);


    /**
     * 判断用户名是否存在
     *
     * @return  1:存在 0：不存在
     *
     */

    int exsitsUsername(@Param("username") String username);

    /**
     * 判断邮箱是否存在
     *
     * @return  1:存在 0：不存在
     *
     */

    int exsitsEmail(@Param("email") String email);


    UserInfo findByUsernameAndPassword(@Param("user") UserInfo userInfo);

    /**
     * 根据用户名查询密保问题
     */
    String findQuestionByUsername(@Param("username")String username);
    /**
     * 根据用户名等查询
     */
    int findByUsernameAndQuestionAndAnswer(@Param("user") UserInfo userInfo);

    /**
     * 根据用户名修改
     */
    int updateUserPassword(@Param("username")String username,@Param("password")String password);
}