package com.yiming.dao;

import static org.junit.Assert.*;

import javax.transaction.Transactional;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yiming.controller.BaseTest;
import com.yiming.entity.User;

import junit.framework.Assert;

public class UserDaoTest extends BaseTest{
    @Autowired
    private UserMapper userDao;

    @Test
    public void loginTest() {
        User user = null;
        user = userDao.login("121112345678", "admin");
        assertEquals(null, user);
        user = userDao.login("12112345678", "adm");
        assertEquals(null, user);
        user = userDao.login("12112345678", "admin");
        assertEquals("teacherLee", user.getNickname());
    }
    
    @Test
    @Transactional
    @Rollback(true)
    public void registerTest() {
        userDao.register("123", "admin", "lee", "leeS");
        User user = userDao.login("123", "admin");
        assertEquals("123", user.getPhoneNum());
        assertEquals("admin", user.getPassword());
        assertEquals("lee", user.getName());
        assertEquals("leeS", user.getNickname());
    }
    @Test
    @Transactional
    @Rollback(true)
    public void updateNicknameByPhoneNumTest() {
        userDao.updateNicknameByPhoneNum("12112345678", "hel");
        User user = userDao.login("12112345678", "admin");
        assertEquals("hel", user.getNickname());
    }
    @Test
    @Transactional
    @Rollback(true)
    public void updatePasswordByPhoneNumTest() {
        userDao.updatePasswordByPhoneNum("12112345678", "hel");
        User user = userDao.login("12112345678", "admin");
        assertEquals(null, user);
        user = userDao.login("12112345678", "hel");
        assertEquals("hel", user.getPassword());
    }
    @Test
    public void getUserByNicknameTest() {
        User user = userDao.getUserByNickname("teacherLee");
        assertEquals("teacherLee", user.getNickname());
    }
    @Test
    public void getUserByPhoneNumTest() {
        User user = userDao.getUserByPhoneNum("12112345678");
        assertEquals("teacherLee", user.getNickname());
    }

}
