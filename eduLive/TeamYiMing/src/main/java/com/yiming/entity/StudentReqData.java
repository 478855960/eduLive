package com.yiming.entity;

public class StudentReqData {
    String reqType, phoneNum, name, nickname, isStudent, liveRoomNum, otherInfo;

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

    public String getReqType() {
        return reqType;
    }

    public void setReqType(String reqType) {
        this.reqType = reqType;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
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

    public String getIsStudent() {
        return isStudent;
    }

    public void setIsStudent(String isStudent) {
        this.isStudent = isStudent;
    }

    public String getLiveRoomNum() {
        return liveRoomNum;
    }

    public void setLiveRoomNum(String liveRoomNum) {
        this.liveRoomNum = liveRoomNum;
    }

    public StudentReqData(String reqType, String phoneNum, String name, String nickname, String isStudent,
            String liveRoomNum) {
        super();
        this.reqType = reqType;
        this.phoneNum = phoneNum;
        this.name = name;
        this.nickname = nickname;
        this.isStudent = isStudent;
        this.liveRoomNum = liveRoomNum;
    }

    public StudentReqData() {
        super();
        // TODO Auto-generated constructor stub
    }


}
