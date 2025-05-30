package com.pygeton.nibot.communication.function;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.pygeton.nibot.communication.entity.Message;
import com.pygeton.nibot.communication.entity.Request;
import com.pygeton.nibot.communication.entity.params.GetMsgParams;
import com.pygeton.nibot.communication.entity.params.SendMsgParams;
import com.pygeton.nibot.communication.entity.params.SetGroupBanParams;
import com.pygeton.nibot.communication.event.IResponseHandler;
import com.pygeton.nibot.communication.websocket.WebSocketClient;

public abstract class Function {

    String[] rawMessage;//接收到的消息分段
    SendMsgParams sendMsgParams;//发送消息参数
    GetMsgParams getMsgParams;//获取消息参数
    SetGroupBanParams setGroupBanParams;//群单人禁言参数
    int handleParam;//接收响应时的处理参数
    volatile boolean isHandled = false;//响应处理标识符

    public static SerializeConfig getConfig(){
        SerializeConfig config = new SerializeConfig();
        config.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
        return config;
    }

    public void setRawMessage(Message message){
        rawMessage = message.getRawMessage().split(" ");
    }

    public void sendMessage(){
        Request<SendMsgParams> request = new Request<>("send_msg", sendMsgParams);
        System.out.println(JSONObject.toJSONString(request, getConfig()));
        WebSocketClient.sendMessage(JSONObject.toJSONString(request, getConfig()));
    }

    public void getMessage(IResponseHandler handler){
        Request<GetMsgParams> request = new Request<>("get_msg", getMsgParams);
        WebSocketClient.setResponding(true);
        WebSocketClient.setResponseHandler(handler);
        System.out.println(JSONObject.toJSONString(request, getConfig()));
        WebSocketClient.sendMessage(JSONObject.toJSONString(request, getConfig()));
    }

    public void setGroupBan(IResponseHandler handler){
        Request<SetGroupBanParams> request = new Request<>("set_group_ban", setGroupBanParams);
        WebSocketClient.setResponding(true);
        WebSocketClient.setResponseHandler(handler);
        System.out.println(JSONObject.toJSONString(request, getConfig()));
        WebSocketClient.sendMessage(JSONObject.toJSONString(request, getConfig()));
    }
}
