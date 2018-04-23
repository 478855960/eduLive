package com.yiming.socket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.web.context.ContextLoader;

import com.alibaba.fastjson.JSON;
import com.yiming.dao.LiveRoomMapper;
import com.yiming.dao.UserMapper;
import com.yiming.entity.LiveRoom;
import com.yiming.entity.StudentReqData;
import com.yiming.entity.TeacherOpData;
import com.yiming.entity.User;
import com.yiming.service.LiveRoomService;
import com.yiming.util.Constant;
import com.yiming.util.GetHttpSessionConfigurator;

@ServerEndpoint(value = "/websocket/studentList/{isStudent}/{phoneNum}/{name}/{liveroomNum}", configurator= GetHttpSessionConfigurator.class)//这里是一个类注解，告诉虚拟机该类被注解为一个WebSocket端点
public class StudentManager {
  //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的
    private static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    private static CopyOnWriteArraySet<StudentManager> webSocketSet = new CopyOnWriteArraySet<StudentManager>();
    private static ConcurrentMap<String,List<StudentManager>> webSocketMap = new  ConcurrentHashMap();
    private static ConcurrentMap<String,StudentManager> teacherMap = new ConcurrentHashMap();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    private HttpSession httpSession;
    private static List<User> users = new LinkedList<>();
    private User user = new User();
    private String liveroomNum;


    /**
     * 连接建立成功调用的方法
     * @param session  可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数
     */
    @OnOpen
    public void onOpen(
            @PathParam("isStudent")String isStudent,
            @PathParam("phoneNum")String phoneNum,
            @PathParam("name")String name,
            @PathParam("liveroomNum")String liveroomNum,
            Session session,
            EndpointConfig config){
        if(null == isStudent || "".equals(isStudent)) {
            return;
        }
        System.out.println("studentListWS open");
        this.user.setIsStudent(isStudent);
        this.user.setPhoneNum(phoneNum);
        this.user.setName(name);
        this.user.setBanned(false);
        this.liveroomNum = liveroomNum;
        this.session = session;
        if("0".equals(isStudent)) {
            teacherMap.put(liveroomNum, this);
            if(!webSocketMap.containsKey(liveroomNum)) {
                List<StudentManager> students = new LinkedList<StudentManager>();
                webSocketMap.put(liveroomNum, students);
            }
        } else {
            List<StudentManager> students = webSocketMap.get(liveroomNum);
            students.add(this);
            webSocketMap.put(liveroomNum, students);
        }
        addOnlineCount();           //在线数加1
//      this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
    }
    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(
            @PathParam("isStudent")String isStudent,
            @PathParam("phoneNum")String phoneNum,
            @PathParam("liveroomNum")String liveroomNum,
            Session session){
        if("0".equals(isStudent)) {
            webSocketMap.remove(liveroomNum);
        } else {
            List<StudentManager> students = webSocketMap.get(liveroomNum);
            if(null == students) {
                return;
            }
            students.remove(this);
        }
        System.out.println("studentListWS close");
        subOnlineCount();           //在线数减1
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     * @throws IOException
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        System.out.println("message: " + message);
        TeacherOpData teacherOp = JSON.parseObject(message, TeacherOpData.class);
        if("query".equals(teacherOp.getType())) {
            String retStr = refreshStudentList(teacherOp.getLiveroomNum());
            this.sendMessage(retStr);
        } else if("banStu".equals(teacherOp.getType())) {
            StudentManager stuManager = findStudentManager(teacherOp.getLiveroomNum(), teacherOp.getPhoneNum());
            stuManager.user.setBanned(true);
            stuManager.sendMessage("banned");
            String retStr = refreshStudentList(teacherOp.getLiveroomNum());
            this.sendMessage(retStr);
        } else if("cancelBanStu".equals(teacherOp.getType())) {
            StudentManager stuManager = findStudentManager(teacherOp.getLiveroomNum(), teacherOp.getPhoneNum());
            stuManager.user.setBanned(false);
            stuManager.sendMessage("cancelBanned");
            String retStr = refreshStudentList(teacherOp.getLiveroomNum());
            this.sendMessage(retStr);
        } else if("addBlacklist".equals(teacherOp.getType())) {
            addBlackList(teacherOp.getLiveroomNum(), teacherOp.getPhoneNum());
            StudentManager stuManager = findStudentManager(teacherOp.getLiveroomNum(), teacherOp.getPhoneNum());
            stuManager.user.setInBlacklist(true);
            stuManager.sendMessage("inBlacklist");
            webSocketMap.get(teacherOp.getLiveroomNum()).remove(stuManager);
            String retStr = refreshStudentList(teacherOp.getLiveroomNum());
            this.sendMessage(retStr);
        } else if("ppt".equals(teacherOp.getType())) {
            List<StudentManager> studentManagers = webSocketMap.get(teacherOp.getLiveroomNum());
            for(StudentManager studentManager: studentManagers) {
                studentManager.sendMessage(message);
            }
        } else if("editor".equals(teacherOp.getType())) {
            List<StudentManager> studentManagers = webSocketMap.get(teacherOp.getLiveroomNum());
            for(StudentManager studentManager: studentManagers) {
                studentManager.sendMessage(message);
            }
        }
    }
    /**
     * 发生错误时调用
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error){
    }

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException{
        this.session.getBasicRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        StudentManager.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        StudentManager.onlineCount--;
    }
    public static String refreshStudentList(String liveroomNum) {
        List<StudentManager> students = webSocketMap.get(liveroomNum);
        List<User> retList = new ArrayList<>();
        for(StudentManager item: students) {
            retList.add(item.user);
        }
        String retStr = JSON.toJSONString(retList);
        return retStr;
    }
    public static StudentManager findStudentManager(String liveroomNum, String studentPhoneNum) {
        List<StudentManager> students = webSocketMap.get(liveroomNum);
        for(StudentManager student: students) {
            if(student.user.getPhoneNum().equals(studentPhoneNum)) {
                return  student;
            }
        }
        return null;
    }
    public static void addBlackList(String liveroomNum, String phoneNum) {
        LiveRoomMapper liveRoomMapper = (LiveRoomMapper) ContextLoader.getCurrentWebApplicationContext().getBean("liveRoomMapper");
        LiveRoom liveroom = liveRoomMapper.getLiveroomByRoomNum(liveroomNum);
        if(liveroom == null) {
            return;
        }
        String blackListStr = liveroom.getBlackList();
        if(blackListStr == null) {
            blackListStr = "";
        }
        List<String> blackList = Arrays.asList(blackListStr.split(","));
        if(!blackList.contains(phoneNum)) {
            blackListStr = blackListStr.concat(phoneNum + ",");
        }
        liveRoomMapper.updateBlacklistByPhoneNum(liveroomNum, blackListStr);
    }
}
