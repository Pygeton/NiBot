package com.pygeton.nibot.communication.websocket;

import com.alibaba.fastjson.JSONObject;
import com.pygeton.nibot.communication.entity.Message;
import com.pygeton.nibot.communication.entity.Response;
import com.pygeton.nibot.communication.event.EventHandler;
import com.pygeton.nibot.communication.event.IResponseHandler;
import com.pygeton.nibot.communication.task.ReconnectTask;
import jakarta.websocket.*;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@ClientEndpoint
public class Client {

    private Session session;
    private static Client INSTANCE;
    private static IResponseHandler responseHandler;
    private volatile static boolean connecting = false;
    private volatile static boolean responding = false;

    public Client(String url) throws DeploymentException, IOException {
        session = ContainerProvider.getWebSocketContainer().connectToServer(this, URI.create(url));
    }

    public synchronized static boolean connect(String url){
        try {
            INSTANCE = new Client(url);
            connecting = false;
            return true;
        }
        catch (Exception e){
            System.out.println("连接失败");
            e.printStackTrace();
            return false;
        }
    }

    public synchronized static void reconnect(){
        if(!connecting) {
            connecting = true;
            if (INSTANCE != null) {
                INSTANCE.session = null;
                INSTANCE = null;
            }
            ReconnectTask.execute();
        }
    }

    @OnOpen
    public void onOpen(Session session){
        System.out.println("连接成功");
    }

    @OnMessage
    public void onMessage(String json){
        if(responding){
            //超时响应机制可能存在问题导致消息重叠，需要修复
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

            Runnable task = () -> {
                responseHandler.timeout();
                setResponding(false);
            };

            ScheduledFuture<?> future = executor.schedule(task, 5, TimeUnit.SECONDS);

            try {
                Response response = JSONObject.parseObject(json,Response.class);
                boolean handleRspFlag = response.getStatus() != null;
                if(handleRspFlag){
                    if(responseHandler != null && response.getStatus().equals("ok")){
                        System.out.println(response);
                        responseHandler.handle(response);
                        setResponding(false);
                        future.cancel(true);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                executor.shutdown();
            }
        }
        else {
            Message message = JSONObject.parseObject(json,Message.class);
            boolean handleMsgFlag = message.getPost_type() != null;
            boolean hideFlag = false;
            if(handleMsgFlag){
                switch (message.getPost_type()){
                    case "message" -> {
                        message.toSegmentList();
                        EventHandler.traverse(message);
                    }
                    case "meta_event" -> hideFlag = true;
                }
                if (!hideFlag) System.out.println(json);
            }
        }
    }

    @OnClose
    public void onClose(Session session){
        System.out.println("连接关闭");
        reconnect();
    }

    @OnError
    public void onError(Session session,Throwable throwable){
        System.out.println("连接异常：" + throwable.getMessage());
    }

    public static void sendMessage(String json){
        Client.INSTANCE.session.getAsyncRemote().sendText(json);
    }

    public static void setResponseHandler(IResponseHandler handler){
        responseHandler = handler;
    }

    public static void setResponding(boolean status){
        responding = status;
    }

}
