package com.yiming.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;

import com.yiming.dao.LiveRoomMapper;
import com.yiming.entity.LiveRoom;

@Service
public class LiveRoomService {
    @Autowired
    LiveRoomMapper liveRoomMapper;

    /**
     * @return List<LiveRoom> 返回直播表中所有行的信息
     */
    public List<LiveRoom> getAllLiveRoomInfo(){
        LiveRoomMapper liveRoomMapper = (LiveRoomMapper) ContextLoader.getCurrentWebApplicationContext().getBean("liveRoomMapper");
        List<LiveRoom> liveRoomInfo = new ArrayList<>();
        liveRoomInfo = liveRoomMapper.getAllLiveRoom();
        if(liveRoomInfo == null)
            return null;
        else {
            return liveRoomInfo;
        }
    }

    /**
     *
     * @param liveroomNum 直播房间号
     * @param phoneNum 被禁言用户电话号
     * @param otherInfo 用于显示禁言/解除禁言的指示信息
     * @return true是完成操作,false出现异常
     */
    public boolean updateGagList(String liveroomNum, String phoneNum, String otherInfo) {
        LiveRoom liveroom = liveRoomMapper.getLiveroomByRoomNum(liveroomNum);
        if(liveroom == null) {
            return false;
        }
        String gagStr = liveroom.getGagList();
        if(gagStr == null) {
            gagStr = "";
        }
        List<String> gagList = Arrays.asList(gagStr.split(","));
        if("addGag".equals(otherInfo)) {
            if(!gagList.contains(phoneNum)) {
                gagStr = gagStr.concat(phoneNum + ",");
            }
        } else {
            if(gagList.contains(phoneNum)) {
                gagList.remove(phoneNum);
                gagStr = String.join(",", (String[]) gagList.toArray(new String[gagList.size()]));
            }
        }
        liveRoomMapper.updateGaglistByRoomNum(liveroomNum, gagStr);
        return true;
    }
	@Autowired
	LiveRoomMapper liveRoomDao;
	
	/**
	 * @return List<LiveRoom> 返回直播表中所有行的信息
	 */
	public List<LiveRoom> getAllLiveRoomInfo(){
		LiveRoomMapper liveRoomMapper = (LiveRoomMapper) ContextLoader.getCurrentWebApplicationContext().getBean("liveRoomMapper");
		List<LiveRoom> liveRoomInfo = new ArrayList<>();
		liveRoomInfo = liveRoomMapper.getAllLiveRoom();
		if(liveRoomInfo == null)
			return null;
		else {
			return liveRoomInfo;
		}
	}
	
	/**
	 * @return List<LiveRoom> 返回模糊查询的直播表中所有行的信息
	 */
	public List<LiveRoom> getQueriedLiveRoomInfo(String otherInfo){
		LiveRoomMapper liveRoomMapper = (LiveRoomMapper) ContextLoader.getCurrentWebApplicationContext().getBean("liveRoomMapper");
		List<LiveRoom> liveRoomInfo = new ArrayList<>();
		liveRoomInfo = liveRoomMapper.getQueriedLiveRoom(otherInfo);
		if(liveRoomInfo == null)
			return null;
		else {
			return liveRoomInfo;
		}
	}
	
	/**
	 * @return String 返回当前直播间的学生黑名单信息
	 */
	public String getBlackList(String teacherId){
		LiveRoomMapper liveRoomMapper = (LiveRoomMapper) ContextLoader.getCurrentWebApplicationContext().getBean("liveRoomMapper");
		String result = "";
		result = liveRoomMapper.getBlackList(teacherId);
		if(result == null || result == "") {
			System.out.println("hhh");
			return null;
		}
		else {
			return result;
		}
	}
}
