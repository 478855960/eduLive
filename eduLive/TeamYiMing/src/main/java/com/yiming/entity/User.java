package com.yiming.entity;

import java.util.Date;

public class User {
	private String phoneNum;
	private String password;
	private String name;
	private String nickname;
	private Date registerTime;
	private String isStudent;

	public User(String phoneNum, String password, String name, String nickname, Date registerTime, String isStudent) {
		super();
		this.phoneNum = phoneNum;
		this.password = password;
		this.name = name;
		this.nickname = nickname;
		this.registerTime = registerTime;
		this.isStudent = isStudent;
	}

	public User() {
		super();
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	public String getIsStudent() {
		return isStudent;
	}

	public void setIsStudent(String isStudent) {
		this.isStudent = isStudent;
	}

	@Override
	public String toString() {
		return "User [phoneNum=" + phoneNum + ", password=" + password + ", name=" + name + ", nickname=" + nickname
				+ ", registerTime=" + registerTime + ", isStudent=" + isStudent + "]";
	}

}
