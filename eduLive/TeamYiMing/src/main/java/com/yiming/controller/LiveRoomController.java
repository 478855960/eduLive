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

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
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
import com.yiming.dao.LiveRoomMapper;
import com.yiming.entity.LiveRoom;
import com.yiming.entity.StuGagBanData;
import com.yiming.entity.User;
import com.yiming.service.LiveRoomService;
import com.yiming.util.CheckSumBuilder;
import com.yiming.util.Constant;
import com.yiming.util.PPT2ImageUtil;
import com.yiming.util.SendVerificationCode;

@Controller
@RequestMapping(value = "/liveroom")
public class LiveRoomController {
    @Autowired
    LiveRoomMapper liveRoomDao;
    @Autowired
    private HttpSession session;
    @Autowired
    private LiveRoomService liveRoomService;
    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value = "/getAllLiveRoomInfo.action", method = RequestMethod.POST)
    @ResponseBody
    public String showLiveList(){
    	String result;
    	LiveRoomService liveRoomService = new LiveRoomService();
        List<LiveRoom> liveRoomInfo = new ArrayList<>();
        liveRoomInfo = liveRoomService.getAllLiveRoomInfo();
        if(liveRoomInfo == null) {
        	result = "failed";
        }
        else {
        	result = JSON.toJSONString(liveRoomInfo);
        }
        return result;
    }
    /**
     *
     * @param file
     *            前台传来的文件
     * @return
     */
    @RequestMapping(value = "/upload.action", method = RequestMethod.POST)
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file) {
        String imgURLs = "";
        try {
            String realFileName = file.getOriginalFilename(); // 获取文件名
            if (!(realFileName.substring(realFileName.length() - 3, realFileName.length()).equals("ppt") || 
                    realFileName.substring(realFileName.length() - 4, realFileName.length()).equals("pptx"))) {
                return "";
            }
            String ctxPath = session.getServletContext().getRealPath("/") + "fileupload/"; // 获取当前web服务器项目路径
            File dirPath = new File(ctxPath); // 创建文件夹
            if (!dirPath.exists()) {
                dirPath.mkdir();
            }
            File uploadFile = new File(ctxPath + realFileName); // 创建文件
            FileCopyUtils.copy(file.getBytes(), uploadFile); // Copy文件
            String imgPath = ctxPath + realFileName + "_images/"; // 处理图片
            String fileURL = "http://" + request.getServerName() + ":" + request.getServerPort() + "/" + request.getContextPath() + "//fileupload//" + realFileName;
            session.setAttribute(Constant.SOURCEPATH, fileURL);
            if (realFileName.substring(realFileName.length() - 3, realFileName.length()).equals("ppt")) {
                imgURLs = PPT2ImageUtil.getPPTImage(imgPath, ctxPath, realFileName, fileURL + "_images//" );
            } else if (realFileName.substring(realFileName.length() - 4, realFileName.length()).equals("pptx")) {
                imgURLs = PPT2ImageUtil.getPPTXImage(imgPath, ctxPath, realFileName, fileURL + "_images//");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "failure";
        }
        return imgURLs;
    }
    
    /**
    *
    * @param file
    *            前台传来的封面
    * @return
    */
    @RequestMapping(value = "/cover.action", method = RequestMethod.POST)
    @ResponseBody
    public String cover(@RequestParam("cover") MultipartFile file) {
        try {
            if (file == null) {
                return "success";
            }
            String realFileName = file.getOriginalFilename(); // 获取文件名
            if (!(realFileName.substring(realFileName.length() - 3, realFileName.length()).equals("jpg")
                    || realFileName.substring(realFileName.length() - 3, realFileName.length()).equals("png"))) {
                return "failure";
            }
            String ctxPath = session.getServletContext().getRealPath("/") + "coverupload/"; // 获取当前web服务器项目路径
            File dirPath = new File(ctxPath); // 创建文件夹
            if (!dirPath.exists()) {
                dirPath.mkdir();
            }
            File uploadFile = new File(ctxPath + realFileName); // 创建文件
            String fileURL = "http://" + request.getServerName() + ":" + request.getServerPort() + "/" + request.getContextPath() + "//fileupload//" + realFileName;
            session.setAttribute(Constant.IMGPATH, fileURL);
            FileCopyUtils.copy(file.getBytes(), uploadFile); // Copy文件
        } catch (Exception ex) {
            ex.printStackTrace();
            return "failure";
        }
        return "succuess";
    }
    
    /**
     * 发起直播
     * @param room
     * @return failure 发起失败   success 发起成功
     */
    @RequestMapping(value = "/initiate.action", method = RequestMethod.POST)
    @ResponseBody
    public String doInitiate(@RequestBody LiveRoom room) {
        try {
            User sessionUser = (User) session.getAttribute(Constant.USER);
            if (sessionUser == null) {
                return "relogin";
            }
            room.setLiveRoomNum(sessionUser.getPhoneNum());
            room.setTeacherId(sessionUser.getPhoneNum());
            room.setImgPath((String) session.getAttribute(Constant.IMGPATH));
            room.setSourcePath((String) session.getAttribute(Constant.SOURCEPATH));
            liveRoomDao.initiate(room.getLiveRoomNum(), room.getRoomName(), room.getTeacherId(), 
                    room.getSourcePath(), room.getImgPath(), room.getIsRecording()); 
        } catch (Exception e) {
            e.printStackTrace();
            return "failure";
        }
        return "success";
    }
    
    /**
     * 返回教学资源路径
     * @param room
     * @return failure 失败   或   资源路径
     */
    @RequestMapping(value = "/getSourcePath.action", method = RequestMethod.POST)
    @ResponseBody
    public String getSourcePath(@RequestBody LiveRoom room) {
        String srcPath = "";
        try {
            srcPath = liveRoomDao.getSourcePath(room.getLiveRoomNum());
        } catch (Exception e) {
            e.printStackTrace();
            return "failure";
        }
        return srcPath;
    }

    @RequestMapping(value = "/addGaglist.action", method = RequestMethod.POST)
    @ResponseBody
    public String addGaglist(@RequestBody StuGagBanData gagStudent) {
        String liveroomNum = gagStudent.getLiveroomNum();
        String phoneNum = gagStudent.getPhoneNum();
        String otherInfo = gagStudent.getOtherInfo();
        boolean status = liveRoomService.updateGagList(liveroomNum, phoneNum, otherInfo);
        String retMsg = gagStudent.getOtherInfo();
        if(status) {
            retMsg = "failure";
        }
        return retMsg;
    }
}
