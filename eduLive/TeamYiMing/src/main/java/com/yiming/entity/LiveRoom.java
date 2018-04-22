package com.yiming.entity;

import java.security.Timestamp;
import java.util.Date;

public class LiveRoom {
    private String liveRoomNum;
    private String roomName;
    private String teacherId;
    private Date startTime;
    private String sourcePath;
    private String imgPath;
    private char isRecording;
    private String gagList;
    private String blackList;
    
    public LiveRoom(String liveRoomNum, String roomName, String teacherId, Date startTime, String sourcePath,
            String imgPath, char isRecording, String gagList, String blackList) {
        super();
        this.liveRoomNum = liveRoomNum;
        this.roomName = roomName;
        this.teacherId = teacherId;
        this.startTime = startTime;
        this.sourcePath = sourcePath;
        this.imgPath = imgPath;
        this.isRecording = isRecording;
        this.gagList = gagList;
        this.blackList = blackList;
    }

    public LiveRoom() {
        super();
    }

    public String getLiveRoomNum() {
        return liveRoomNum;
    }

    public void setLiveRoomNum(String liveRoomNum) {
        this.liveRoomNum = liveRoomNum;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public char getIsRecording() {
        return isRecording;
    }

    public void setIsRecording(char isRecording) {
        this.isRecording = isRecording;
    }

    public String getGagList() {
        return gagList;
    }

    public void setGagList(String gagList) {
        this.gagList = gagList;
    }

    public String getBlackList() {
        return blackList;
    }

    public void setBlackList(String blackList) {
        this.blackList = blackList;
    }

    @Override
    public String toString() {
        return "LiveRoom [liveRoomNum=" + liveRoomNum + ", roomName=" + roomName + ", teacherId=" + teacherId
                + ", startTime=" + startTime + ", sourcePath=" + sourcePath + ", imgPath=" + imgPath + ", isRecording="
                + isRecording + ", gagList=" + gagList + ", blackList=" + blackList + "]";
    }



}
