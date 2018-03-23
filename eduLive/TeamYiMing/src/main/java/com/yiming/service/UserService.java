package com.yiming.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yiming.dao.UserDao;
import com.yiming.entity.User;

@Service
public class UserService {
    @Resource
    UserDao userDao;

    /**
     *
     * @param phoneNum 当前用户的电话号
     * @param newNickname 用户新的昵称
     * @return -1代表昵称重名 0表示出现错误修改出错 1表示修改成功
     */
    public int setUserNickname(String phoneNum, String newNickname) {
        try {
            User user = null;
            user = userDao.getUserByNickname(newNickname);
            if(null == user) {
                return -1;
            }
            userDao.updateNicknameByPhoneNum(phoneNum, newNickname);
            return 1;
        }catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    /**
     *
     * @param phoneNum 当前用户的电话号
     * @param newPassword 用户的新电话号
     * @return  -0表示出现错误修改出错 1表示修改成功
     */
    public int setUserPassword(String phoneNum, String newPassword) {
        try {
            userDao.updatePasswordByPhoneNum(phoneNum, newPassword);
            return 1;
        }catch(Exception e) {
            return 0;
        }
    }
}