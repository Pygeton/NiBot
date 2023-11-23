package com.pygeton.nibot.communication.function;

import com.alibaba.fastjson.JSONObject;
import com.pygeton.nibot.communication.entity.Message;
import com.pygeton.nibot.communication.entity.Request;
import com.pygeton.nibot.communication.entity.params.GetMsgParams;
import com.pygeton.nibot.communication.entity.params.SendMsgParams;
import com.pygeton.nibot.communication.entity.params.SetGroupBanParams;
import com.pygeton.nibot.communication.event.IResponseHandler;
import com.pygeton.nibot.communication.websocket.Client;

public abstract class Function {

    String[] rawMessage;//接收到的消息分段
    SendMsgParams sendMsgParams;//发送消息参数
    GetMsgParams getMsgParams;//获取消息参数
    SetGroupBanParams setGroupBanParams;//群单人禁言参数
    int handleParam;//接收响应时的处理参数
    volatile boolean isHandled = false;//响应处理标识符

    public void setRawMessage(Message message){
        rawMessage = message.getRaw_message().split(" ");
    }

    public void sendMessage(){
        Request<SendMsgParams> request = new Request<>("send_msg", sendMsgParams);
        System.out.println(JSONObject.toJSONString(request));
        Client.sendMessage(JSONObject.toJSONString(request));
    }

    public void getMessage(IResponseHandler handler){
        Request<GetMsgParams> request = new Request<>("get_msg", getMsgParams);
        Client.setResponding(true);
        Client.setResponseHandler(handler);
        System.out.println(JSONObject.toJSONString(request));
        Client.sendMessage(JSONObject.toJSONString(request));
    }

    public void setGroupBan(IResponseHandler handler){
        Request<SetGroupBanParams> request = new Request<>("set_group_ban", setGroupBanParams);
        Client.setResponding(true);
        Client.setResponseHandler(handler);
        System.out.println(JSONObject.toJSONString(request));
        Client.sendMessage(JSONObject.toJSONString(request));
    }
}
