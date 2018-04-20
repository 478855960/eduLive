package com.yiming.dao;

import java.util.Date;

import com.yiming.entity.LiveRoom;

public interface LiveRoomMapper {
    public void initiate(String liveRoomNum, String roomName, String teacherId, String sourcePath,
            String imgPath, char isRecording);
    public String getSourcePath (String liveRoomNum);
}
