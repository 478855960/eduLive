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
        Assert.assertEquals(null, user);
        user = userDao.login("12112345678", "adm");
        Assert.assertEquals(null, user);
        user = userDao.login("12112345678", "admin");
        Assert.assertEquals("teacherLee", user.getNickname());
    }
    
    @Test
    public void registerTest() {
        userDao.register("123", "admin", "lee", "leeS");
        User user = userDao.login("123", "admin");
        Assert.assertEquals("123", user.getPhoneNum());
        Assert.assertEquals("admin", user.getPassword());
        Assert.assertEquals("leeS", user.getName());
        Assert.assertEquals("leeS", user.getNickname());
    }
    @Test
    public void updateNicknameByPhoneNumTest() {
        userDao.updateNicknameByPhoneNum("121112345678", "hel");
        User user = userDao.login("121112345678", "admin");
        Assert.assertEquals("hel", user.getNickname());
        userDao.updateNicknameByPhoneNum("121112345678", "teacherLee");
        Assert.assertEquals("teacherLee", user.getNickname());
    }
    @Test
    public void updatePasswordByPhoneNumTest() {
        userDao.updatePasswordByPhoneNum("121112345678", "hel");
        User user = userDao.login("121112345678", "admin");
        Assert.assertEquals("hel", user.getPassword());
        userDao.updatePasswordByPhoneNum("121112345678", "admin");
        Assert.assertEquals("hel", user.getPassword());
        
    }
    @Test
    public void getUserByNicknameTest() {
        User user = userDao.getUserByNickname("teacherLee");
        Assert.assertEquals("teacherLee", user.getNickname());
    }
    @Test
    public void getUserByPhoneNumTest() {
        User user = userDao.getUserByPhoneNum("121112345678");
        Assert.assertEquals("teacherLee", user.getNickname());
        
    }

}
