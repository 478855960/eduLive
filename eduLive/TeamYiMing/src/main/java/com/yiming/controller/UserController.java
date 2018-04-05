package com.yiming.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.yiming.dao.UserMapper;
import com.yiming.entity.User;
import com.yiming.service.UserService;
import com.yiming.util.CheckSumBuilder;
import com.yiming.util.Constant;
import com.yiming.util.SendVerificationCode;

@Controller
@RequestMapping(value = "/user")
public class UserController {
	@Autowired
	UserMapper userDao;
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
    public String login(@RequestBody User reqUser){
		User user = null;
		user = userDao.login(reqUser.getPhoneNum(), reqUser.getPassword());
		if(user != null){
			session.setAttribute(Constant.USER, user);
			return "success";
		}else{
			return "failure";
		}
		
	}
	/**
	 * 检查输入的旧密码是否正确
	 */
	@RequestMapping(value = "/checkPassword.action", method = RequestMethod.POST)
    @ResponseBody
    public int checkPassword(@RequestBody User reqUser){
		User sessionUser = (User) session.getAttribute(Constant.USER);
		if(sessionUser.getPassword().equals(reqUser.getPassword())) {
			return 1;
		}else{
			return 0;
		}
		
	}
	/**
	 * checkPhoneNum.action
	 */
	@RequestMapping(value = "/checkNickname.action", method = RequestMethod.POST)
    @ResponseBody
	public int checkNickname(@RequestBody User user) {
		User result = userDao.getUserByNickname(user.getNickname());
		if(result == null) {
			return 0;
		}else {
			return 1;
		}
	}
	/**
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/checkPhoneNum.action", method = RequestMethod.POST)
    @ResponseBody
	public int checkPhoneNum(@RequestBody User user) {
		User result = userDao.getUserByPhoneNum(user.getPhoneNum());
		if(result == null) {
			return 0;
		}else {
			return 1;
		}
	}
	
	/**
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @description 用户注册发送验证码
	 */
	@RequestMapping(value = "/sendMessage.action", method = RequestMethod.POST)
    @ResponseBody
    public int sendMessage(@RequestBody User user) throws ClientProtocolException, IOException {
		SendVerificationCode sendCode = new SendVerificationCode(user.getPhoneNum());
		String code = sendCode.sendVerificationCode();
		System.out.println("code is: " + code);
		if(code.length() != 6) {
			return 0;
		}else {
			session.setAttribute("VerificationCode", code);
			return 1;
		}
	}
	
	/**
	 * @description 验证用户输入的验证码的正确性
	 */
	@RequestMapping(value = "/isVerificationCodeTrue.action", method = RequestMethod.POST)
    @ResponseBody
	public int isVerificationCodeTrue(@RequestBody User user) {
		System.out.println("sdadadadadadada");
		System.out.println("code si: " + user.getVerificationCode());
		String verificationCode = (String)session.getAttribute("VerificationCode");
		if(user.getVerificationCode().equals(verificationCode)) {
			return 1;
		}else {
			return 0;
		}
	}
	/**
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * 
	 */
	@RequestMapping(value = "/modifySendMessage.action", method = RequestMethod.POST)
    @ResponseBody
    public int modifySendMessage(@RequestBody User user) throws ClientProtocolException, IOException {
		SendVerificationCode sendCode = new SendVerificationCode(user.getPhoneNum());
		String code = sendCode.sendVerificationCode();
		System.out.println("code is: " + code);
		return 0;
	}
	/**
	 * 
	 * @param user
	 * @return 0注册失败 1注册成功
	 */
	@RequestMapping(value = "/userRegister.action",method = RequestMethod.POST)
	@ResponseBody
	public void register(@RequestBody User user) {
		userDao.register(user.getPhoneNum(),user.getPassword(),user.getName(),user.getNickname());
//		if(result == null)
//			return 0;
//		else
//			return 1;
	}
	/**
	 *
	 * @param nickname 用户想要修改的昵称
	 * @return
	 */
	@RequestMapping(value="/userModifyNickname.action",method = RequestMethod.POST)
	@ResponseBody
	public String userModifyNickName(@RequestBody User user) {
	    User sessionUser = (User) session.getAttribute(Constant.USER);
	    if(null == sessionUser) {
	        return "relogin";
	    }
	    int returnStatus = userService.setUserNickname(sessionUser.getPhoneNum(), user.getNickname());
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
	 * @return
	 */
	@RequestMapping(value="/userModifyPassword.action",method = RequestMethod.POST)
	@ResponseBody
	public String userModifyPassword(@RequestBody User user) {
	    User sessionUser = (User) session.getAttribute(Constant.USER);
	    if(null == sessionUser) {
	        return "relogin";
	    }
	    int returnStatus = userService.setUserPassword(sessionUser.getPhoneNum(), user.getPassword());
	    if(1 == returnStatus) {
	        sessionUser = userDao.getUserByPhoneNum(sessionUser.getPhoneNum());
	        session.setAttribute(Constant.USER, sessionUser);
	        System.err.println("succ");
	        return "success";
	    }else {
	        return "modifyFail";
	    }
	}
	
	/**
	 * 
	 * @param file 前台传来的文件
	 * @return
	 */
	@RequestMapping(value="/upload.action",method = RequestMethod.POST)
	@ResponseBody
	public String upload(@RequestParam("file") MultipartFile file) {
		System.out.println("Conroller in!!");
		System.out.println(file.getOriginalFilename());
		try {
			//获取文件名
			String realFileName = file.getOriginalFilename();
			// 获取当前web服务器项目路径
			String ctxPath = session.getServletContext().getRealPath("/")+ "fileupload/";
			System.out.println(ctxPath);
			// 创建文件夹
			File dirPath = new File(ctxPath);
			if (!dirPath.exists()) {
				dirPath.mkdir();
			}
			// 创建文件
			File uploadFile = new File(ctxPath + realFileName);
			// Copy文件
			FileCopyUtils.copy(file.getBytes(), uploadFile);
		} catch (Exception ex) {
			return "failure";
		}
		return "success";
	}
}

