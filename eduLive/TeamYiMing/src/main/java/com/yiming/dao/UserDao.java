package com.yiming.dao;

import java.util.Date;

public interface UserDao {
	public User login(String phoneNum,String password);
	
	public void register(String phoneNum,String password,String name,String nickname,Date registerTime,String isStudent);
}
