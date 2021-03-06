package com.yiming.entity;

import java.util.Date;

public class User {
    private String phoneNum;
    private String password;
    private String name;
    private String nickname;
    private Date registerTime;
    private String isStudent;
    private String verificationCode;
    private boolean isBanned;
    private boolean inBlacklist;
    @Override
    public String toString() {
        return "User [phoneNum=" + phoneNum + ", password=" + password + ", name=" + name + ", nickname=" + nickname
                + ", registerTime=" + registerTime + ", isStudent=" + isStudent + ", verificationCode="
                + verificationCode + ", isBanned=" + isBanned + ", inBlacklist=" + inBlacklist + "]";
    }
    public User() {
        super();
        // TODO Auto-generated constructor stub
    }
    public User(String phoneNum, String password, String name, String nickname, Date registerTime, String isStudent,
            String verificationCode, boolean isBanned, boolean inBlacklist) {
        super();
        this.phoneNum = phoneNum;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.registerTime = registerTime;
        this.isStudent = isStudent;
        this.verificationCode = verificationCode;
        this.isBanned = isBanned;
        this.inBlacklist = inBlacklist;
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
    public String getVerificationCode() {
        return verificationCode;
    }
    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }
    public boolean isBanned() {
        return isBanned;
    }
    public void setBanned(boolean isBanned) {
        this.isBanned = isBanned;
    }
    public boolean isInBlacklist() {
        return inBlacklist;
    }
    public void setInBlacklist(boolean inBlacklist) {
        this.inBlacklist = inBlacklist;
    }
}
