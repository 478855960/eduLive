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
import com.yiming.dao.LiveRoomMapper;
import com.yiming.entity.LiveRoom;
import com.yiming.entity.StuGagBanData;
import com.yiming.entity.StudentReqData;
import com.yiming.entity.TeacherOpData;
import com.yiming.entity.User;
import com.yiming.service.LiveRoomService;
import com.yiming.util.Constant;
import com.yiming.util.PPT2ImageUtil;

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

    
    /**
     * @author xs
     * @desc 获取所有正在直播的直播间信息
     * @return JSON对象数组的字符串
     */
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
     * @author xs
     * @desc 模糊查询正在直播的直播间
     * @return JSON对象数组的字符串
     */
    @RequestMapping(value = "/queryLiveRoomInfo.action", method = RequestMethod.POST)
    @ResponseBody
    public String queryLiveList(@RequestBody StudentReqData studentReqData){
    	String queryInfo,result;
    	LiveRoomService liveRoomService = new LiveRoomService();
        List<LiveRoom> liveRoomInfo = new ArrayList<>();
        queryInfo = studentReqData.getOtherInfo();
        liveRoomInfo = liveRoomService.getQueriedLiveRoomInfo(queryInfo);
        if(liveRoomInfo == null) {
        	result = "failed";
        }
        else {
        	result = JSON.toJSONString(liveRoomInfo);
        }
        System.out.println(result);
        return result;
    }
    
    /**
     * @author xs
     * @desc 获取学生是否被拉黑（即当前学生是否有进入该老师直播间的权限）
     * @return JSON对象数组的字符串
     */
    @RequestMapping(value = "/queryStatus.action", method = RequestMethod.POST)
    @ResponseBody
    public int queryCurStuStatus(@RequestBody LiveRoom liveRoom){
    	int result = 0;//0代表该学生没有权限进入该教师直播间，即在黑名单中
    	System.out.println(liveRoom.getTeacherId());
    	String teacherID;
    	User user = (User) session.getAttribute(Constant.USER);
    	LiveRoomService liveRoomService = new LiveRoomService();
        String blackList = "";
        blackList = liveRoomService.getBlackList(liveRoom.getTeacherId());
        
        if(blackList == null || blackList.contains(user.getPhoneNum()) == false) {
        	result = 1;
        }
        System.out.println(blackList);
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
            System.out.println(ctxPath);
            File dirPath = new File(ctxPath); // 创建文件夹
            if (!dirPath.exists()) {
                dirPath.mkdir();
            }
            File uploadFile = new File(ctxPath + realFileName); // 创建文件
            String fileURL = "http://" + request.getServerName() + ":" + request.getServerPort() + "/" + request.getContextPath() + "//coverupload//" + realFileName;
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
    /**
     * 将学生添加到禁言列表
     * @param gagStudent 禁言学生的信息
     * @return
     */
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
    /**
     * 获取开启录播的post请求
     * @param teacherOpData
     * @return
     */
    @RequestMapping(value="/postVideo.action", method = RequestMethod.POST)
    @ResponseBody
    public String changePage(@RequestBody TeacherOpData teacherOpData) {
        String type = teacherOpData.getType();
        if("stop".equals(type)) {
            String channel = "1000";
            channel = teacherOpData.getOtherInfo();
            String cmd = "stop commend";
            try{
                Runtime rt = Runtime.getRuntime();
                Process proc = rt.exec(cmd);
            }catch (Exception e){
                e.printStackTrace();
                return "fail";
            }
        }else {
            String channel = "1000";
            channel = teacherOpData.getOtherInfo();
            String cmd = "./recorder_local "
                    + "--appId a86334acf5c04a6aa7a85b66d0767612 "
                    + "--uid 0 "
                    + "--channel " + channel + " "
                    + " --appliteDir /root/agoraRecord/Agora_Recording_SDK_for_Linux_FULL/bin/ "
                    + "--channelProfile 1 "
                    + "--isVideoOnly 1 "
                    + "--recordFileRootDir /root/\r\n";
            try{
                Runtime rt = Runtime.getRuntime();
                //执行命令, 最后一个参数，可以使用new File("path")指定运行的命令的位置
                Process proc = rt.exec(cmd);
            }catch (Exception e){
                e.printStackTrace();
                return "fail";
            }
        }
        return "succeed";
    }
}
