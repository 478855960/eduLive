package com.yiming.service;

import java.util.ArrayList;
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
