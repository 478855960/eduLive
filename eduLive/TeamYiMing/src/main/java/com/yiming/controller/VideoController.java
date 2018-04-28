package com.yiming.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSON;
import com.yiming.dao.VideoMapper;
import com.yiming.entity.Video;
import com.yiming.entity.StuGagBanData;
import com.yiming.entity.StudentReqData;
import com.yiming.entity.TeacherOpData;
import com.yiming.entity.User;
import com.yiming.util.Constant;
import com.yiming.util.PPT2ImageUtil;

@Controller
@RequestMapping(value = "/video")
public class VideoController {
    @Autowired
    VideoMapper videoDao;
    @Autowired
    private HttpSession session;
    @Autowired
    private HttpServletRequest request;

    /**
     * @author xs
     * @desc 获取所有正在直播的直播间信息
     * @return JSON对象数组的字符串
     */
    @RequestMapping(value = "/getAllVideoInfo.action", method = RequestMethod.POST)
    @ResponseBody
    public String showLiveList() {
        String result;
        List<Video> videoInfo = new ArrayList<>();
        videoInfo = videoDao.getAllVideo();
        if (videoInfo == null) {
            result = "failed";
        } else {
            result = JSON.toJSONString(videoInfo);
        }
        return result;
    }

}
