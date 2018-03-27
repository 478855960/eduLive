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
     * 鐢ㄦ埛鐨勭櫥褰曞嚱鏁�
     * 鍒ゆ柇鐢佃瘽瀵嗙爜鏄惁姝ｇ‘
     * 濡傛灉姝ｇ‘锛岃繑鍥炩�渟uccess鈥濓紝涓嶆纭繑鍥炩�渇ailure鈥濓紙瀛楃涓诧級
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
	 * @param nickname 鐢ㄦ埛鎯宠淇敼鐨勬樀绉�
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
	 * @param oldPassword 鐢ㄦ潵楠岃瘉鐨勬棫瀵嗙爜
	 * @param newPassword 闇�瑕佷慨鏀圭殑鏂板瘑鐮�
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
