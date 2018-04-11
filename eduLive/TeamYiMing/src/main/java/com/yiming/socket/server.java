package com.yiming.socket;

import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/websocket/server")//这里是一个类注解，告诉虚拟机该类被注解为一个WebSocket端点  
public class server {

    @OnMessage
    public String echo(String clientMessage){
        return clientMessage;  
    }
}
