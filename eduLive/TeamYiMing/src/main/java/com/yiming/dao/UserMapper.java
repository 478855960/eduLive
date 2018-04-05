package com.yiming.dao;

import java.util.Date;

import com.yiming.entity.User;

public interface UserMapper {
	public User login(String phoneNum,String password);
	public void register(String phoneNum,String password,String name,String nickname);
	public void updateNicknameByPhoneNum(String phoneNum, String newNickname);
	public void updatePasswordByPhoneNum(String phoneNum, String newPassword);
	public User getUserByNickname(String nickname);
	public User getUserByPhoneNum(String phoneNum);
}
