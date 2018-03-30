package com.yiming.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yiming.dao.UserDao;
import com.yiming.entity.User;
import com.yiming.service.UserService;
import com.yiming.util.CheckSumBuilder;
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
	 * 
	 */
	@RequestMapping(value = "/sendMessage.action", method = RequestMethod.POST)
    @ResponseBody
    public int sendMessage(@RequestBody User user) throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		
        HttpPost httpPost = new HttpPost(Constant.SERVER_URL);
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        /*
         * 参考计算CheckSum的java代码，在上述文档的参数列表中，有CheckSum的计算文档示例
         */
        String checkSum = CheckSumBuilder.getCheckSum(Constant.APP_SECRET, Constant.NONCE, curTime);

        // 设置请求的header
        httpPost.addHeader("AppKey", Constant.APP_KEY);
        httpPost.addHeader("Nonce", Constant.NONCE);
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        // 设置请求的的参数，requestBody参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        /*
         * 1.如果是模板短信，请注意参数mobile是有s的，详细参数配置请参考“发送模板短信文档”
         * 2.参数格式是jsonArray的格式，例如 "['13888888888','13666666666']"
         * 3.params是根据你模板里面有几个参数，那里面的参数也是jsonArray格式
         */
        nvps.add(new BasicNameValuePair("templateid", Constant.TEMPLATEID));
        nvps.add(new BasicNameValuePair("mobile", user.getPhoneNum()));
        nvps.add(new BasicNameValuePair("codeLen", Constant.CODELEN));

        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

        // 执行请求
        HttpResponse response = httpclient.execute(httpPost);
        /*
         * 1.打印执行结果，打印结果一般会200、315、403、404、413、414、500
         * 2.具体的code有问题的可以参考官网的Code状态表
         */
        System.out.println(EntityUtils.toString(response.getEntity(), "utf-8"));

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
