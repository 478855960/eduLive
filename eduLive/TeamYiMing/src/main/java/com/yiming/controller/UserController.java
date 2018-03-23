package com.yiming.controller;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yiming.dao.UserDao;
import com.yiming.entity.User;
import com.yiming.service.UserService;
import com.yiming.util.Constant;

@Controller
@RequestMapping(value = "/user")
public class UserController {
	@Autowired
	UserDao userDao;
	@Autowired
	private  HttpSession session;
	@Resource
	private UserService userService;
	
	/**
     * 用户的登录函数
     * 判断电话密码是否正确
     * 如果正确，返回“success”，不正确返回“failure”（字符串）
     */
	@RequestMapping(value = "/userLogin.action", method = RequestMethod.POST)
    @ResponseBody
    public String login(String phoneNum,String password){
		User user = new User();
		user = userDao.login(phoneNum, password);
		if(user != null){
			session.setAttribute(Constant.USER, user);
			return "success";
		}else{
			return "failure";
		}
		
	}
	@RequestMapping(value = "/userRegister.action",method = RequestMethod.POST)
	@ResponseBody
	public void register(String phoneNum,String password,String name,String nickname,Date registerTime,String isStudent) {
		userDao.register(phoneNum, password, name, nickname, registerTime, isStudent);
	}
	/**
	 *
	 * @param nickname 用户想要修改的昵称
	 * @return
	 */
	@RequestMapping(value="/userModifyNickname.action",method = RequestMethod.POST)
	@ResponseBody
	public String userModifyNickName(String nickname) {
	    User sessionUser = (User) session.getAttribute(Constant.USER);
	    if(null == sessionUser) {
	        return "relogin";
	    }
	    int returnStatus = userService.setUserNickname(sessionUser.getPhoneNum(), nickname);
	    if(-1 == returnStatus) {
	        return "nicknameExist";
	    }else if(0 == returnStatus) {
	        return "modifyFail";
	    }else {
	        return "success";
	    }
	}
	/**
	 *
	 * @param oldPassword 用来验证的旧密码
	 * @param newPassword 需要修改的新密码
	 * @return
	 */
	@RequestMapping(value="/userModifyPassword.action",method = RequestMethod.POST)
	@ResponseBody
	public String userModifyPassword(String oldPassword, String newPassword) {
	    User sessionUser = (User) session.getAttribute(Constant.USER);
	    if(null == sessionUser) {
	        return "relogin";
	    }
	    if(oldPassword == null || !oldPassword.equals("root")) {
	        System.out.println("wrongpass");
	        return "wrongOldPassword";
	    }
	    int returnStatus = userService.setUserPassword(sessionUser.getPhoneNum(), newPassword);
	    if(1 == returnStatus) {
	        sessionUser = userDao.getUserByPhoneNum(sessionUser.getPhoneNum());
	        session.setAttribute(Constant.USER, sessionUser);
	        System.err.println("succ");
	        return "success";
	    }else {
	        return "modifyFail";
	    }
	}
}
