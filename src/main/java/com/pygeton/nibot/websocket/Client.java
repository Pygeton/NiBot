package com.pygeton.nibot.websocket;

import com.alibaba.fastjson.JSONObject;
import com.pygeton.nibot.entity.Message;
import com.pygeton.nibot.event.EventHandler;
import jakarta.websocket.*;

import java.io.IOException;
import java.net.URI;

@ClientEndpoint
public class Client {

    private Session session;
    private static Client INSTANCE;

    public Client(String url) throws DeploymentException, IOException {
        session = ContainerProvider.getWebSocketContainer().connectToServer(this, URI.create(url));
    }

    public synchronized static boolean connect(String url){
        try {
            INSTANCE = new Client(url);
            return true;
        }
        catch (DeploymentException | IOException e){
            e.printStackTrace();
            return false;
        }
    }

    @OnOpen
    public void onOpen(Session session){
        System.out.println("连接成功");
    }

    @OnMessage
    public void onMessage(String json){
        Message message = JSONObject.parseObject(json,Message.class);
        boolean handleFlag = message.getPost_type() != null;
        boolean hideFlag = false;
        if(handleFlag){
            switch (message.getPost_type()){
                case "message" -> EventHandler.traverse(message);
                case "meta_event" -> hideFlag = true;
            }
            if (!hideFlag) System.out.println(json);
        }
    }

    @OnClose
    public void onClose(Session session){
        System.out.println("连接关闭，正在请求重连...");
        Client.connect("ws://127.0.0.1:9099");
    }

    @OnError
    public void onError(Session session,Throwable throwable){
        System.out.println("连接异常" + throwable.getMessage());
    }

    public static void sendMessage(String json){
        Client.INSTANCE.session.getAsyncRemote().sendText(json);
    }
}
