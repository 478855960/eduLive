package com.yiming.socket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

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
import com.yiming.dao.UserMapper;
import com.yiming.entity.User;
import com.yiming.util.Constant;
import com.yiming.util.GetHttpSessionConfigurator;

@ServerEndpoint(value = "/websocket/whiteboard/{isStudent}/{phoneNum}/{liveroomNum}")
public class Whiteboard {
  //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的
    private static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    private static CopyOnWriteArraySet<Whiteboard> webSocketSet = new CopyOnWriteArraySet<Whiteboard>();
    private static ConcurrentMap<String,List<Whiteboard>> webSocketMap = new  ConcurrentHashMap();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
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
            @PathParam("liveroomNum")String liveroomNum,
            Session session){
        if(null == isStudent || "".equals(isStudent)) {
            System.out.println("连接失败");
            return;
        }
//        System.out.println("whiteboard open");
        this.user.setIsStudent(isStudent);
        this.user.setPhoneNum(phoneNum);
        this.liveroomNum = liveroomNum;
        this.session = session;
        if("0".equals(isStudent)) {
            if(!webSocketMap.containsKey(liveroomNum)) {
                List<Whiteboard> students = new LinkedList<Whiteboard>();
                webSocketMap.put(liveroomNum, students);
            }
        } else {
            List<Whiteboard> students = webSocketMap.get(liveroomNum);
            students.add(this);
            webSocketMap.put(liveroomNum, students);
        }
//        this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
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
            List<Whiteboard> students = webSocketMap.get(liveroomNum);
            students.remove(this);
        }
        webSocketSet.remove(this);
        subOnlineCount();           //在线数减1
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println(message);
        List<Whiteboard> students = webSocketMap.get(this.liveroomNum);
        System.out.println("whiteboard stusize " + students.size());
//        System.out.println("message" + result);
        //群发消息
        for(Whiteboard item: students){
            try {
                System.out.println(item.user.getPhoneNum());
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
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
        System.out.println("错误whiteboard 在线人数" + onlineCount);
        error.printStackTrace();
    }

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException{
        System.out.println("send msg");
        this.session.getBasicRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        Whiteboard.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        Whiteboard.onlineCount--;
    }
}
