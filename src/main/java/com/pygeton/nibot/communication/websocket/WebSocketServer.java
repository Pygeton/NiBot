package com.pygeton.nibot.communication.websocket;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/api/websocket")
public class WebSocketServer {

    private static final Map<String, Session> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session){
        sessions.put(session.getId(), session);
        System.out.println("Vue连接成功");
    }

    @OnClose
    public void onClose(Session session){
        sessions.remove(session.getId());
        System.out.println("Vue连接关闭");
    }

    @OnMessage
    public void onMessage(Session session,String message){
        sessions.remove(session.getId());
        System.out.println(message);
    }

    @OnError
    public void onError(Session session,Throwable throwable){
        System.out.println("Vue连接异常：" + throwable.getMessage());
    }

    public synchronized void sendMessage(String json) {
        sessions.values().forEach(session -> {
            session.getAsyncRemote().sendText(json);
        });
    }
}
