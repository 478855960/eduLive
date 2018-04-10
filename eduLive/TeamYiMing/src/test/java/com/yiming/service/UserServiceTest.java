package com.yiming.service;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.yiming.controller.BaseTest;
import com.yiming.dao.UserMapper;
import com.yiming.entity.User;

public class UserServiceTest extends BaseTest{
    @Autowired
    UserService userService;
    @Autowired
    UserMapper userMapper;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testSetUserNickname() {
        int status = userService.setUserNickname("12112345678", "jack");
        assertEquals(-1, status);
        status = userService.setUserNickname("12112345678", "helloWorld");
        User user = userMapper.login("12112345678", "admin");
        assertEquals(1, status);
        assertEquals("helloWorld", user.getNickname());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testSetUserPassword() {
        int status = userService.setUserPassword("12112345678", "jack");
        assertEquals(1, status);
        User user = userMapper.login("12112345678", "admin");
        assertEquals(null, user);
        user = userMapper.login("12112345678", "jack");
        assertEquals("jack", user.getPassword());
    }

}
