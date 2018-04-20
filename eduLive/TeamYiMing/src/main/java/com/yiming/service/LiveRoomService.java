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
}
