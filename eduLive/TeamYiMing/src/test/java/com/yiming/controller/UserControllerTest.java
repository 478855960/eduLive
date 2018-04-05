package com.yiming.controller;

import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml","classpath:springMVC.xml"})
public class UserControllerTest {
//    private MockMvc mockMvc;
//    @Autowired
//    private UserController userController;
//    @Autowired
//    protected WebApplicationContext wac;
//    @Before
//    public void setUp() throws Exception {
//        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();  //初始化MockMvc对象
//    }
//    @Test
//    public void userModifyPasswordtest() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.post("/user/userModifyNickname.action").param("jack"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.view().name("relogin"));
//    }

}
