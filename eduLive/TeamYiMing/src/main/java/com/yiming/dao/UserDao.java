package com.yiming.dao;

import java.util.Date;

public interface UserDao {
	public void register(String phoneNum,String password,String name,String nickname,Date registerTime,String isStudent);
}
