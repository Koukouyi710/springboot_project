package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.exception.MyException;
import com.neuedu.pojo.UserInfo;

import java.util.List;

public interface IUserService {
    /**
     * 登录接口
     * */
    public ServerResponse login(UserInfo userInfo) throws MyException;
    /**
     * 注册接口
     * */
    public ServerResponse register(UserInfo userInfo);
    /**
     * 查询密保问题
     * */
    public ServerResponse forget_get_question(UserInfo userInfo);
    /**
     * 提交答案
     * */
    public ServerResponse forget_check_answer(UserInfo userInfo);
    /**
     * 重置密码
     * */
    public ServerResponse forget_reset_password(UserInfo userInfo, String forgetToken);
    /**
     * 校验用户名或邮箱是否有效
     * */
    public ServerResponse check_valid(String str,String type);
    /**
     * 登录状态下修改密码
     * */
    public ServerResponse reset_password(UserInfo userInfo,String passwordOld,String passwordNew);
    /**
     * 登录状态下修改信息
     * */
    public ServerResponse update_information(UserInfo user);
    /**
     * 用户列表
     * */
    public ServerResponse list(Integer pageSize,Integer pageNum);

        /**
         * 查询用户
         * */
    public List<UserInfo> findAll()throws MyException;
    /**
     * 修改用户
     * */
    public int updateUserInfo(UserInfo userInfo)throws MyException;
    /**
     * 删除用户
     * */
    public int deleteUserInfo(int userInfoId)throws MyException;
    /**
     * 添加用户
     * */
    public int addUserInfo(UserInfo userInfo)throws MyException;
    /**
     * 根据用户id查询用户信息
     */
    public UserInfo findUserInfoById(int userInfoId);

}
