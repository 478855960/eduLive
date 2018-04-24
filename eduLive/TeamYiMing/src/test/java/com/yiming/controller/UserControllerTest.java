package com.yiming.controller;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.yiming.entity.User;
import com.yiming.util.Constant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={"file:src/main/resources/testConf/spring-dao.xml","file:src/main/resources/testConf/spring-service.xml","file:src/main/resources/testConf/spring-web.xml"})
public class UserControllerTest {
    protected MockMvc mockMvc;
    protected MockHttpSession session;

    @Autowired
    protected WebApplicationContext wac;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        session = new MockHttpSession();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testLogin() throws UnsupportedEncodingException, Exception {
        User user = new User();
        user.setPhoneNum("12112345678");
        user.setPassword("admin");
        String reqJSONSucc = JSONObject.toJSONString(user);
        user.setPhoneNum("12112345678");
        user.setPassword("hs");
        String reqJSONErr = JSONObject.toJSONString(user);
        System.out.println(reqJSONSucc);
        System.out.println(reqJSONErr);
        String responseString = mockMvc.perform
                (
                    post("/user/userLogin.action")          //请求的url,请求的方法是get
                    //get("/user/showUser2")          //请求的url,请求的方法是get
                    .contentType(MediaType.APPLICATION_JSON)//数据的格式
//                    .param("phoneNum","15956531060")   //添加参数(可以添加多个)
//                    .param("password","hsx")   //添加参数(可以添加多个)
                    .content(reqJSONErr)
                )
                .andExpect(status().isOk())    //返回的状态是200
                .andExpect(content().string("failure"))
//                .andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();   //将相应的数据转换为字符串
//            System.out.println("-----返回的json = " + responseString);


        responseString = mockMvc.perform
                (
                    post("/user/userLogin.action")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(reqJSONSucc)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("success"))
                .andReturn().getResponse().getContentAsString();
//        System.out.println("-----返回的json = " + responseString);
    }

    @Test
    @Ignore("前台代码需要修改")
    public void testRegister() throws UnsupportedEncodingException, Exception {
        User user = new User();
        user.setPhoneNum("12112345678");
        user.setPassword("admin");
        String reqJSONErr = JSONObject.toJSONString(user);
        user.setPhoneNum("12111111111");
        user.setPassword("admin");
        String reqJSONSucc = JSONObject.toJSONString(user);
        String responseString = "";
        responseString = mockMvc.perform
                (
                    post("/user/userRegister.action")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(reqJSONErr)
                )
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        responseString = mockMvc.perform
                (
                    post("/user/userRegister.action")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(reqJSONSucc)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("1"))
//                .andDo(print())
                .andReturn().getResponse().getContentAsString();
    }
    @Rollback(true)
    @Transactional
    @Test
    public void testUserModifyNickName() throws UnsupportedEncodingException, Exception {
        User user = new User();
        user.setPhoneNum("12112345678");
        user.setNickname("teacherLee");
        String reqJSONExist = JSONObject.toJSONString(user);
        user.setPhoneNum("12112345678");
        user.setNickname("nick");
        String reqJSONSucc = JSONObject.toJSONString(user);
        String responseString = "";
        
        responseString = mockMvc.perform
                (
                    post("/user/userModifyNickname.action")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(reqJSONExist)
                    .session((MockHttpSession) this.getLoginSession())
                )
                .andExpect(status().isOk())
                .andExpect(content().string("nicknameExist"))
                .andReturn().getResponse().getContentAsString();
        responseString = mockMvc.perform
                (
                    post("/user/userModifyNickname.action")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(reqJSONExist)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("relogin"))
                .andReturn().getResponse().getContentAsString();
        responseString = mockMvc.perform
                (
                    post("/user/userModifyNickname.action")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(reqJSONSucc)
                    .session((MockHttpSession) this.getLoginSession())
                )
                .andExpect(status().isOk())
                .andExpect(content().string("success"))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testUserModifyPassword() throws UnsupportedEncodingException, Exception {
        User user = new User();
        user.setPhoneNum("12112345678");
        user.setPassword("jack");
        String reqJSONSucc = JSONObject.toJSONString(user);
        String responseString = "";
        responseString = mockMvc.perform
                (
                    post("/user/userModifyPassword.action")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(reqJSONSucc)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("relogin"))
                .andReturn().getResponse().getContentAsString();
        responseString = mockMvc.perform
                (
                    post("/user/userModifyPassword.action")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(reqJSONSucc)
                    .session((MockHttpSession) this.getLoginSession())
                )
                .andExpect(status().isOk())
                .andExpect(content().string("success"))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    @Ignore
    public void testUpload() {
    }
    @Test
    public void testCheckPassword() throws Exception {
        User user = new User();
        user.setPassword("admin");
        String requestBody = JSONObject.toJSONString(user);
        mockMvc.perform
            (
                post("/user/checkPassword.action")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .session((MockHttpSession) this.getLoginSession())
            )
            .andExpect(content().string("1"));
            
        user.setPassword("123");
        String errorBody = JSONObject.toJSONString(user);
        mockMvc.perform
        (
            post("/user/checkPassword.action")
            .contentType(MediaType.APPLICATION_JSON)
            .content(errorBody)
            .session((MockHttpSession) this.getLoginSession())
        )
        .andExpect(content().string("0"));
    }
    
    @Test
    public void testCheckNickname() throws Exception {
        User user = new User();
        user.setNickname("teacherLee");
        String requestBody = JSONObject.toJSONString(user);
        mockMvc.perform
        (
            post("/user/checkNickname.action")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)
        )
        .andExpect(content().string("1"));
        
        user.setNickname("xx");
        String errorBody = JSONObject.toJSONString(user);
        mockMvc.perform
        (
            post("/user/checkNickname.action")
            .contentType(MediaType.APPLICATION_JSON)
            .content(errorBody)
        )
        .andExpect(content().string("0"));
    }
    
    @Test
    public void testCheckPhoneNum() throws Exception {
        User user = new User();
        user.setPhoneNum("12112345678");
        String requestBody = JSONObject.toJSONString(user);
        mockMvc.perform
        (
            post("/user/checkPhoneNum.action")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)
        )
        .andExpect(content().string("1"));
        
        user.setPhoneNum("123456");
        String errorBody = JSONObject.toJSONString(user);
        mockMvc.perform
        (
            post("/user/checkPhoneNum.action")
            .contentType(MediaType.APPLICATION_JSON)
            .content(errorBody)
        )
        .andExpect(content().string("0"));
    }
    
    @Test
    @Ignore
    public void testSendMessage() throws Exception {
        User user = new User();
        user.setPhoneNum("12112345678");
        String requestBody = JSONObject.toJSONString(user);
        mockMvc.perform
        (
            post("/user/sendMessage.action")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)
        )
        .andExpect(content().string("1"));
        
        user.setPhoneNum("123456");
        String errorBody = JSONObject.toJSONString(user);
        mockMvc.perform
        (
            post("/user/sendMessage.action")
            .contentType(MediaType.APPLICATION_JSON)
            .content(errorBody)
        )
        .andExpect(content().string("0"));
    }
    
    @Test
    public void testIsVerificationCodeTrue() throws Exception {
        session.setAttribute("VerificationCode", "233333");
        User user = new User();
        user.setVerificationCode("233333");
        String requestBody = JSONObject.toJSONString(user);
        mockMvc.perform
        (
            post("/user/isVerificationCodeTrue.action")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)
            .session(session)
        )
        .andExpect(content().string("1"));
        
        user.setVerificationCode("123456");
        String errorBody = JSONObject.toJSONString(user);
        mockMvc.perform
        (
            post("/user/isVerificationCodeTrue.action")
            .contentType(MediaType.APPLICATION_JSON)
            .content(errorBody)
            .session(session)
        )
        .andExpect(content().string("0"));
    }
    
    @Test
    public void testModifySendMessage() throws Exception {
        User user = new User();
        user.setPhoneNum("12112345678");
        String requestBody = JSONObject.toJSONString(user);
        mockMvc.perform
        (
            post("/user/modifySendMessage.action")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)
        )
        .andExpect(content().string("0"));
    }
    /** 
     * 获取登入信息session 
     * @return 
     * @throws Exception 
     */  
    private HttpSession getLoginSession() throws Exception{  
        User user = new User();
        user.setPhoneNum("12112345678");
        user.setPassword("admin");
        String reqJSONSucc = JSONObject.toJSONString(user);
        MvcResult result = this.mockMvc  
                                .perform(post("/user/userLogin.action")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(reqJSONSucc)
                                        )
                                .andExpect(status().isOk())  
                                .andReturn();  
        return result.getRequest().getSession();
    }
}
