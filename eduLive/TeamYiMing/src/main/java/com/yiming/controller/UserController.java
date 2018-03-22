package com.yiming.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yiming.dao.UserDao;
import com.yiming.entity.User;

@Controller
public class UserController {
	@Autowired
	UserDao userdao;
	
	/**
     * 用户的登录函数
     * 判断电话密码是否正确
     * 如果正确，返回“success”，不正确返回“failure”（字符串）
     */
	
//	@RequestMapping(value = "/userLogin", method = RequestMethod.POST)
//    @ResponseBody
//    public String login(){
//		return "success";
//	}
	
	@RequestMapping(value = "/userRegister",method = RequestMethod.POST)
	@ResponseBody
	public void register(String phoneNum,String password,String name,String nickname,Date registerTime,String isStudent) {
		userdao.register(phoneNum, password, name, nickname, registerTime, isStudent);
	}
}
