package com.pygeton.nibot.communication.function;

import com.pygeton.nibot.communication.entity.Message;
import com.pygeton.nibot.communication.entity.Response;
import com.pygeton.nibot.communication.entity.data.ImageData;
import com.pygeton.nibot.communication.entity.data.MessageData;
import com.pygeton.nibot.communication.entity.data.ReplyData;
import com.pygeton.nibot.communication.entity.params.GetMsgParams;
import com.pygeton.nibot.communication.entity.params.SendMsgParams;
import com.pygeton.nibot.communication.event.IMessageEvent;
import com.pygeton.nibot.communication.event.IResponseHandler;
import com.pygeton.nibot.repository.pojo.LongData;
import com.pygeton.nibot.repository.service.LongDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class SummonLong extends Function implements IMessageEvent, IResponseHandler {

    @Autowired
    LongDataServiceImpl longDataService;

    @Override
    public int weight() {
        return 98;
    }

    @Override
    public boolean onMessage(Message message) {
        setRawMessage(message);
        if(rawMessage[0].contains("/long")){
            sendMsgParams = new SendMsgParams(message);
            match(message);
            return true;
        }
        else return false;
    }

    private void match(Message message) {
        switch (rawMessage.length){
            case 1 -> {
                if(rawMessage[0].equals("/long")) getLong();
            }
            case 2 -> {
                if(rawMessage[1].equals("add")){
                    MessageData messageData = message.getSegmentList().get(0).getData();
                    if(messageData instanceof ReplyData replyData){
                        if(message.getMessageType().equals("group")){
                            if(message.getGroupId() == 251697087L || message.getGroupId() == 653948081L){
                                addLong(replyData);
                            }
                            else forward(replyData);
                        }
                        else forward(replyData);
                    }
                    else {
                        sendMsgParams.addTextMessageSegment("需要对龙图进行回复才能使用此功能哦，详见/help 4>_<");
                        sendMessage();
                    }
                }
            }
            default -> {
                sendMsgParams.addTextMessageSegment("参数有误，请输入/help 4查看帮助文档>_<");
                sendMessage();
            }
        }
    }

    private void getLong(){
        Random random = new Random();
        int num = random.nextInt((int) longDataService.count());
        LongData data = longDataService.getById(num + 1);
        sendMsgParams.addImageMessageSegment(data.getUrl());
        sendMessage();
    }

    private void addLong(ReplyData replyData){
        getMsgParams = new GetMsgParams(replyData.getId());
        handleParam = 1;
        getMessage(this);
    }

    private void forward(ReplyData replyData){
        getMsgParams = new GetMsgParams(replyData.getId());
        handleParam = 2;
        getMessage(this);
    }

    @Override
    public void handle(Response response) {
        isHandled = true;
        System.out.println(handleParam);
        response.toSegmentList();
        MessageData messageData = response.getSegmentList().get(0).getData();
        if(handleParam == 1){
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
            sendMessage();
        }
        else if(handleParam == 2){
            sendMsgParams.addTextMessageSegment("龙图添加请求发送成功，审核通过后你的龙图就会出现在召唤池里！");
            sendMessage();
            sendMsgParams = new SendMsgParams("group",653948081L);
            sendMsgParams.addTextMessageSegment("收到龙图添加请求，请进行审核。");
            sendMessage();
            sendMsgParams = new SendMsgParams("group",653948081L);
            if(messageData instanceof ImageData imageData){
                sendMsgParams.addImageMessageSegment(imageData.getUrl());
            }
            sendMessage();
        }
    }

    @Override
    public void timeout() {
        if(!isHandled){
            sendMsgParams.addTextMessageSegment("操作失败：响应超时");
            sendMessage();
        }
    }
}
