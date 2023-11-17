package com.pygeton.nibot.communication.function;

import com.alibaba.fastjson.JSONObject;
import com.pygeton.nibot.communication.entity.Request;
import com.pygeton.nibot.communication.entity.params.GetMsgParams;
import com.pygeton.nibot.communication.entity.params.SendMsgParams;
import com.pygeton.nibot.communication.event.IResponseHandler;
import com.pygeton.nibot.communication.websocket.Client;

public abstract class Function {

    SendMsgParams sendMsgParams;//发送消息参数
    GetMsgParams getMsgParams;//获取消息参数
    int handleParam = 0;//接收响应时的处理参数

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
}
