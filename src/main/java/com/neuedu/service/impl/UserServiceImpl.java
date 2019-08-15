package com.neuedu.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.UserInfoMapper;
import com.neuedu.exception.MyException;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IUserService;
import com.neuedu.utils.MD5Utils;
import com.neuedu.utils.TokenCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {
   @Autowired
   UserInfoMapper userInfoMapper;
    @Override
    public ServerResponse login(UserInfo userInfo) throws MyException {
        //step1:参数的非空判断
//        if(userInfo==null){
//            throw new MyException("参数不能为空");
//        }
        if(userInfo.getUsername()==null||userInfo.getUsername().equals("")){
            return ServerResponse.createServerResponseByFail("用户名不能为空!");
        }
        if(userInfo.getPassword()==null||userInfo.getPassword().equals("")){
            return ServerResponse.createServerResponseByFail("密码不能为空!");
        }
        //step2:判断用户名是否存在
          int username_result=userInfoMapper.exsitsUsername(userInfo.getUsername());
          if(username_result==0){
              return ServerResponse.createServerResponseByFail("用户名不存在!");
          }

        //step3:根据用户名和密码登录
        UserInfo userInfo1=userInfoMapper.findByUsernameAndPassword(userInfo);
          if(userInfo1==null)
              return ServerResponse.createServerResponseByFail("用户名或密码不正确!");
        return ServerResponse.createServerResponseBySucess(userInfo1);
    }

    @Override
    public ServerResponse register(UserInfo userInfo) {
        //step1:参数的非空判断
//        if(userInfo==null){
//            throw new MyException("参数不能为空");
//        }
        if(userInfo.getUsername()==null||userInfo.getUsername().equals("")){
            return ServerResponse.createServerResponseByFail("用户名不能为空!");
        }
        if(userInfo.getPassword()==null||userInfo.getPassword().equals("")){
            return ServerResponse.createServerResponseByFail("密码不能为空！");
        }
        if(userInfo.getEmail()==null||userInfo.getEmail().equals("")){
            return ServerResponse.createServerResponseByFail("邮箱不能为空！");
        }
        if(userInfo.getPhone()==null||userInfo.getPhone().equals("")){
            return ServerResponse.createServerResponseByFail("手机不能为空！");
        }
        if(userInfo.getQuestion()==null||userInfo.getQuestion().equals("")){
            return ServerResponse.createServerResponseByFail("请输入密保问题！");
        }
        if(userInfo.getAnswer()==null||userInfo.getAnswer().equals("")){
            return ServerResponse.createServerResponseByFail("请输入密保答案！");
        }
        //step2:判断用户名是否存在
        int username_result=userInfoMapper.exsitsUsername(userInfo.getUsername());
        if(username_result!=0){
            return ServerResponse.createServerResponseByFail("用户名已存在！");
        }
        //step3:判断邮箱是否存在
        int email_result=userInfoMapper.exsitsEmail(userInfo.getEmail());
        if(email_result!=0){
            return ServerResponse.createServerResponseByFail("该邮箱已注册！");
        }
        //step4:注册
        userInfo.setRole(Const.RoleEnum.ROLE_CUSTOMER.getCode());
        int count = userInfoMapper.insert(userInfo);
        if(count>0){
            return ServerResponse.createServerResponseBySucess("注册成功！");
        }

        return ServerResponse.createServerResponseByFail("注册失败。");
    }

    @Override
    public ServerResponse forget_get_question(UserInfo userInfo) {
        //step1:参数的非空判断
//        if(userInfo==null){
//            throw new MyException("参数不能为空");
//        }
        if(userInfo.getUsername()==null||userInfo.getUsername().equals("")){
            return ServerResponse.createServerResponseByFail("用户名不能为空!");
        }

        //step2:判断用户名是否存在
        int username_result=userInfoMapper.exsitsUsername(userInfo.getUsername());
        if(username_result==0){
            return ServerResponse.createServerResponseByFail("用户名不存在！");
        }
        //step3:查找密保问题
        String question = userInfoMapper.findQuestionByUsername(userInfo.getUsername());
        if (question==null||question.equals("")){
            return ServerResponse.createServerResponseByFail("未设置密保问题！");
        }

        return ServerResponse.createServerResponseBySucess(question);
    }

    @Override
    public ServerResponse forget_check_answer(UserInfo userInfo) {

        //step1：参数校验
        if(userInfo.getUsername()==null||userInfo.getUsername().equals("")){
            return ServerResponse.createServerResponseByFail("用户名不能为空!");
        }
        if(userInfo.getQuestion()==null||userInfo.getQuestion().equals("")){
            return ServerResponse.createServerResponseByFail("请输入密保问题！");
        }
        if(userInfo.getAnswer()==null||userInfo.getAnswer().equals("")) {
            return ServerResponse.createServerResponseByFail("请输入密保答案！");
        }
        //step2：根据username，question，answer查询
        int userInfo_result = userInfoMapper.findByUsernameAndQuestionAndAnswer(userInfo);
        if (userInfo_result==0){
            //答案错误
            return ServerResponse.createServerResponseByFail("密保问题答案不正确！");
        }
        //step3：服务端生成一个token保存并将token返回给客户端
        String forgetToken = UUID.randomUUID().toString();
        //guava cache
        TokenCache.set(userInfo.getUsername(),forgetToken);
        return ServerResponse.createServerResponseBySucess(forgetToken);
    }

    @Override
    public ServerResponse forget_reset_password(UserInfo userInfo, String forgetToken) {
        //step1：参数校验
        if(userInfo.getUsername()==null||userInfo.getUsername().equals("")){
            return ServerResponse.createServerResponseByFail("用户名不能为空!");
        }
        if(userInfo.getPassword()==null||userInfo.getPassword().equals("")){
            return ServerResponse.createServerResponseByFail("密码不能为空！");
        }
        if(forgetToken==null||forgetToken.equals("")){
            return ServerResponse.createServerResponseByFail("校验码不能为空！");
        }
        //step2：token校验
       String token = TokenCache.get(userInfo.getUsername());
        if(token==null){
            return ServerResponse.createServerResponseByFail("校验码已过期！");
        }
        if (!token.equals(forgetToken)){
            return ServerResponse.createServerResponseByFail("无效的校验码！");
        }
        //step3：修改密码
        int result = userInfoMapper.updateUserPassword(userInfo.getUsername(),userInfo.getPassword());
        if(result>0){
            //修改成功
            return ServerResponse.createServerResponseBySucess("修改成功！");
        }
        return ServerResponse.createServerResponseByFail("修改失败！");
    }

    @Override
    public ServerResponse check_valid(String str, String type) {
        //step1：参数校验
        if(str==null||str.equals("")){
            return ServerResponse.createServerResponseByFail("用户名或者邮箱不能为空!");
        }
        if(type==null||type.equals("")){
            return ServerResponse.createServerResponseByFail("校验类型参数不能为空！");
        }
        //step2：type：username ->校验用户名str
        //           ：email->校验邮箱str
       if (type.equals("username")){
            int result = userInfoMapper.exsitsUsername(str);
            if (result>0){
                //用户存在
                return ServerResponse.createServerResponseByFail("用户名已存在！");
            }else{
                return ServerResponse.createServerResponseBySucess();
            }
       }else if (type.equals("email")){
           int result = userInfoMapper.exsitsEmail(str);
           if (result>0){
               //邮箱存在
               return ServerResponse.createServerResponseByFail("该邮箱已注册！");
           }else{
               return ServerResponse.createServerResponseBySucess();
           }
       }else{
           return ServerResponse.createServerResponseByFail("参数类型错误！");
       }
        //step3：返回结果
    }

    @Override
    public ServerResponse reset_password(UserInfo userInfo,String passwordOld, String passwordNew) {
        //step1：参数校验
        if(passwordOld==null||passwordOld.equals("")){
            return ServerResponse.createServerResponseByFail("请输入旧密码!");
        }
        if(passwordNew==null||passwordNew.equals("")){
            return ServerResponse.createServerResponseByFail("请输入新密码！");
        }
        //step2：查询信息
        passwordOld = MD5Utils.getMD5Code(passwordOld);
        if (!passwordOld.equals(userInfo.getPassword())){
            return ServerResponse.createServerResponseByFail("旧密码错误！");
        }
        //step3：返回结果
        userInfo.setPassword(MD5Utils.getMD5Code(passwordNew));
        int result = userInfoMapper.updateByPrimaryKey(userInfo);
        if (result>0){
            return ServerResponse.createServerResponseBySucess("密码修改成功！");
        }
        return ServerResponse.createServerResponseByFail("密码修改失败！");
    }

    @Override
    public ServerResponse update_information(UserInfo user) {
        //step1：参数校验
       if (user==null){
           return ServerResponse.createServerResponseByFail("参数不能为空！");
       }
        //step2：更新用户信息
        int result = userInfoMapper.updateByPrimaryKey(user);
        if (result>0){
            return ServerResponse.createServerResponseBySucess("信息修改成功！");
        }
        return ServerResponse.createServerResponseByFail("信息修改失败！");
    }

    @Override
    public ServerResponse list(Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        List<UserInfo> userInfoList=userInfoMapper.selectAll();
        Iterator<UserInfo> userInfoIterator = userInfoList.iterator();
        while (userInfoIterator.hasNext()){
            UserInfo userInfo = userInfoIterator.next();
            userInfo.setPassword("");
        }
        PageInfo pageInfo = new PageInfo(userInfoList);
        return ServerResponse.createServerResponseBySucess(pageInfo);
    }

    @Override
    public List<UserInfo> findAll() throws MyException {
       return userInfoMapper.selectAll();
    }

    @Override
    public int updateUserInfo(UserInfo userInfo) throws MyException {
        return userInfoMapper.updateByPrimaryKey(userInfo);
    }

    @Override
    public int deleteUserInfo(int userInfoId) throws MyException {
        return userInfoMapper.deleteByPrimaryKey(userInfoId);
    }

    @Override
    public int addUserInfo(UserInfo userInfo) throws MyException {
        return userInfoMapper.insert(userInfo);
    }

    @Override
    public UserInfo findUserInfoById(int userInfoId) {
        return userInfoMapper.selectByPrimaryKey(userInfoId);
    }
}
