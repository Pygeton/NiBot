package com.pygeton.nibot.communication.function;

import com.alibaba.fastjson.JSONObject;
import com.pygeton.nibot.communication.entity.Message;
import com.pygeton.nibot.communication.entity.Request;
import com.pygeton.nibot.communication.entity.Response;
import com.pygeton.nibot.communication.entity.data.ImageData;
import com.pygeton.nibot.communication.entity.data.MessageData;
import com.pygeton.nibot.communication.entity.data.ReplyData;
import com.pygeton.nibot.communication.entity.params.GetMsgParams;
import com.pygeton.nibot.communication.entity.params.SendMsgParams;
import com.pygeton.nibot.communication.event.IMessageEvent;
import com.pygeton.nibot.communication.event.IResponseHandler;
import com.pygeton.nibot.communication.websocket.Client;
import com.pygeton.nibot.repository.entity.LongData;
import com.pygeton.nibot.repository.service.LongDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class SummonLong implements IMessageEvent, IResponseHandler {

    SendMsgParams sendMsgParams;

    @Autowired
    LongDataServiceImpl longDataService;

    @Override
    public int weight() {
        return 80;
    }

    @Override
    public boolean onMessage(Message message) {
        String[] rawMessage = message.getRaw_message().split(" ");
        if(rawMessage[0].contains("/long")){
            sendMsgParams = new SendMsgParams(message);
            match(message,rawMessage);
            return true;
        }
        else return false;
    }

    @Override
    public void handle(Response response) {
        response.toSegmentList();
        MessageData messageData = response.getSegmentList().get(0).getData();
        boolean ret = false;
        if(messageData instanceof ImageData imageData){
            ret = longDataService.add(imageData.getUrl());
        }
        if(ret){
            sendMsgParams.addTextMessageSegment("添加龙图到召唤池成功！");
        }
        else {
            sendMsgParams.addTextMessageSegment("添加失败：数据库异常");
        }
        Request<SendMsgParams> request = new Request<>("send_msg", sendMsgParams);
        System.out.println(JSONObject.toJSONString(request));
        Client.sendMessage(JSONObject.toJSONString(request));
    }

    private void match(Message message, String[] rawMessage) {
        switch (rawMessage.length){
            case 1 -> {
                if(rawMessage[0].equals("/long")) getLong();
            }
            case 2 -> {
                if(rawMessage[1].equals("add")){
                    MessageData messageData = message.getSegmentList().get(0).getData();
                    if(messageData instanceof ReplyData replyData){
                        if(message.getMessage_type().equals("group")){
                            if(message.getGroup_id() == 251697087L){
                                addLong(replyData);
                            }
                            else forward(replyData);
                        }
                        else forward(replyData);
                    }
                    else sendMsgParams.addTextMessageSegment("需要对龙图进行回复才能使用此功能哦，详见/help 4。");
                }
            }
            default -> sendMsgParams.addTextMessageSegment("参数有误，请输入/help 4查看帮助文档。");
        }
    }

    private void getLong(){
        Random random = new Random();
        int num = random.nextInt((int) longDataService.count());
        LongData data = longDataService.getById(num + 1);
        sendMsgParams.addImageMessageSegment(data.getUrl());
        Request<SendMsgParams> request = new Request<>("send_msg", sendMsgParams);
        System.out.println(JSONObject.toJSONString(request));
        Client.sendMessage(JSONObject.toJSONString(request));
    }

    private void addLong(ReplyData replyData){
        GetMsgParams getMsgParams = new GetMsgParams(replyData.getId());
        Request<GetMsgParams> request = new Request<>("get_msg", getMsgParams);
        System.out.println(JSONObject.toJSONString(request));
        Client.setResponding();
        Client.setResponseHandler(this);
        Client.sendMessage(JSONObject.toJSONString(request));
    }

    private void forward(ReplyData replyData){

    }

}
