package com.neuedu.controller.portal;

import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IUserService;
import com.neuedu.utils.DateUtils;
import com.neuedu.utils.MD5Utils;
import com.neuedu.vo.UserDetailVO;
import com.neuedu.vo.UserInfoVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/user/")
public class UserController {

    @Autowired
    IUserService userService;

    /**
     *登录
     */
    @RequestMapping(value = "login.do")
    public ServerResponse login(UserInfo userInfo, HttpSession session, HttpServletResponse response){

        String pwd = MD5Utils.getMD5Code(userInfo.getPassword());
        userInfo.setPassword(pwd);
        ServerResponse serverResponse = userService.login(userInfo);
        if(serverResponse.isSucess()){
            UserInfo loginUserInfo = (UserInfo) serverResponse.getData();
            session.setAttribute(Const.CURRENT_USER,loginUserInfo);
            Cookie username_cookie = new Cookie("username",loginUserInfo.getUsername());
            Cookie password_cookie = new Cookie("password",loginUserInfo.getPassword());
            username_cookie.setMaxAge(60*60*24*7);
            password_cookie.setMaxAge(60*60*24*7);

            response.addCookie(username_cookie);
            response.addCookie(password_cookie);
        }
        return serverResponse;
    }

    /**
     *注册
     */
    @RequestMapping(value = "register.do")
    public ServerResponse register(UserInfo userInfo, HttpSession session, HttpServletResponse response){

        String pwd = MD5Utils.getMD5Code(userInfo.getPassword());
        userInfo.setPassword(pwd);
        ServerResponse serverResponse = userService.register(userInfo);

        return serverResponse;
    }

    /**
     *根据用户名查询密保问题
     */
    @RequestMapping(value = "forget_get_question.do")
    public ServerResponse forget_get_question(UserInfo userInfo, HttpSession session, HttpServletResponse response){
        ServerResponse serverResponse = userService.forget_get_question(userInfo);
        return serverResponse;
    }

    /**
     *提交问题答案
     */
    @RequestMapping(value = "forget_check_answer.do")
    public ServerResponse forget_check_answer(UserInfo userInfo, HttpSession session, HttpServletResponse response){
        ServerResponse serverResponse = userService.forget_check_answer(userInfo);
        return serverResponse;
    }

    /**
     *重置密码
     */
    @RequestMapping(value = "forget_reset_password.do")
    public ServerResponse forget_reset_password(UserInfo userInfo,String forgetToken, HttpSession session, HttpServletResponse response){
        String pwd = MD5Utils.getMD5Code(userInfo.getPassword());
        userInfo.setPassword(pwd);
        ServerResponse serverResponse = userService.forget_reset_password(userInfo,forgetToken);
        return serverResponse;
    }

    /**
     *检查用户名或邮箱是否有效
     */
    @RequestMapping(value = "check_valid.do")
    public ServerResponse check_valid(String str,String type){
        ServerResponse serverResponse = userService.check_valid(str,type);
        return serverResponse;
    }

    /**
     *获取登录信息
     */
    @RequestMapping(value = "get_user_info.do")
    public ServerResponse get_user_info(HttpSession session){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENT_USER);

        UserInfoVO userInfoVO = assembleUserInfoVO(userInfo);
        return ServerResponse.createServerResponseBySucess(userInfoVO);
    }

    private UserInfoVO assembleUserInfoVO(UserInfo userInfo){
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setCreateTime(DateUtils.dateToString(userInfo.getCreateTime()));
        userInfoVO.setUsername(userInfo.getUsername());
        userInfoVO.setEmail(userInfo.getEmail());
        userInfoVO.setId(userInfo.getId());
        userInfoVO.setPhone(userInfo.getPhone());
        userInfoVO.setUpdateTime(DateUtils.dateToString(userInfo.getUpdateTime()));
        return userInfoVO;
    }


    /**
     *登录状态下重置密码
     */
    @RequestMapping(value = "reset_password.do")
    public ServerResponse reset_password(HttpSession session,String passwordOld,String passwordNew){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENT_USER);

        return userService.reset_password(userInfo,passwordOld,passwordNew);
    }

    /**
     *登录状态下更新个人信息
     */
    @RequestMapping(value = "update_information.do")
    public ServerResponse update_information(HttpSession session,UserInfo user){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENT_USER);

        user.setId(userInfo.getId());
        return userService.update_information(user);
    }

    /**
     *获取详细信息
     */
    @RequestMapping(value = "get_inforamtion.do")
    public ServerResponse get_inforamtion(HttpSession session){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENT_USER);

        UserDetailVO userDetailVO = assembleUserDetailVO(userInfo);
        return ServerResponse.createServerResponseBySucess(userDetailVO);
    }

    private UserDetailVO assembleUserDetailVO(UserInfo userInfo){
        UserDetailVO userDetailVO = new UserDetailVO();
        userDetailVO.setCreateTime(DateUtils.dateToString(userInfo.getCreateTime()));
        userDetailVO.setUsername(userInfo.getUsername());
        userDetailVO.setEmail(userInfo.getEmail());
        userDetailVO.setId(userInfo.getId());
        userDetailVO.setPhone(userInfo.getPhone());
        userDetailVO.setAnswer(userInfo.getAnswer());
        userDetailVO.setQuestion(userInfo.getQuestion());
        userDetailVO.setRole(userInfo.getRole());
        userDetailVO.setUpdateTime(DateUtils.dateToString(userInfo.getUpdateTime()));
        return userDetailVO;
    }

    /**
     *登出
     */
    @RequestMapping(value = "logout.do")
    public ServerResponse logout(HttpSession session, HttpServletResponse response, HttpServletRequest request){
        Cookie username_cookie = new Cookie("username",null);
        Cookie password_cookie = new Cookie("password",null);
        password_cookie.setMaxAge(0);
        password_cookie.setPath("/user");
        response.addCookie(password_cookie);
        username_cookie.setMaxAge(0);
        username_cookie.setPath("/user");
        response.addCookie(username_cookie);
        session = request.getSession();
        session.invalidate();
        return ServerResponse.createServerResponseBySucess();
    }
}
