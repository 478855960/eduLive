package com.yiming.controller;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yiming.dao.UserDao;
import com.yiming.entity.User;
import com.yiming.util.Constant;

@Controller
public class UserController {
	@Autowired
	UserDao userdao;
	@Autowired
	private  HttpSession session;
	
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
		userdao.register(phoneNum, password, name, nickname, registerTime, isStudent);
	}
}
