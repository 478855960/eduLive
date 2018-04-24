package com.yiming.socket;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.yiming.entity.User;

@ServerEndpoint(value = "/websocket/codeEditor/{isStudent}/{phoneNum}/{liveroomNum}")
public class CodeEditor {
//静态变量，用来记录当前在线连接数。应该把它设计成线程安全的
    private static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    private static CopyOnWriteArraySet<CodeEditor> webSocketSet = new CopyOnWriteArraySet<CodeEditor>();
    private static ConcurrentMap<String,List<CodeEditor>> webSocketMap = new ConcurrentHashMap();
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
        System.out.println("连接codeEditor socket");
        if(null == isStudent || "".equals(isStudent)) {
            return;
        }
        this.user.setIsStudent(isStudent);
        this.user.setPhoneNum(phoneNum);
        this.liveroomNum = liveroomNum;
        this.session = session;
        if("0".equals(isStudent)) {
            if(!webSocketMap.containsKey(liveroomNum)) {
                List<CodeEditor> students = new LinkedList<CodeEditor>();
                webSocketMap.put(liveroomNum, students);
            }
        } else {
            List<CodeEditor> students = webSocketMap.get(liveroomNum);
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
        System.out.println("关闭codeEditor socket");
        if("0".equals(isStudent)) {
            webSocketMap.remove(liveroomNum);
        } else {
            List<CodeEditor> students = webSocketMap.get(liveroomNum);
            if(null == students) {
                return;
            }
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
        List<CodeEditor> students = webSocketMap.get(this.liveroomNum);
        //群发消息
        for(CodeEditor item: students){
            try {
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
       CodeEditor.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
       CodeEditor.onlineCount--;
    }
}
