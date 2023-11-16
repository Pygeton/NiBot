package com.pygeton.nibot.communication.function;

import com.alibaba.fastjson.JSONObject;
import com.pygeton.nibot.communication.entity.Message;
import com.pygeton.nibot.communication.entity.Params;
import com.pygeton.nibot.communication.entity.Request;
import com.pygeton.nibot.communication.entity.data.ImageData;
import com.pygeton.nibot.communication.entity.data.MessageData;
import com.pygeton.nibot.communication.event.IMessageEvent;
import com.pygeton.nibot.communication.websocket.Client;
import com.pygeton.nibot.repository.entity.LongData;
import com.pygeton.nibot.repository.service.LongDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class SummonLong implements IMessageEvent {

    Request<Params> request;
    Params params;

    @Autowired
    LongDataServiceImpl longDataService;

    @Override
    public int weight() {
        return 80;
    }

    @Override
    public boolean onMessage(Message message) {
        String[] rawMessage = message.getRaw_message().split(" ");
        if(rawMessage[0].equals("/long")){
            params = new Params(message);
            try {
                match(message,rawMessage);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
            request = new Request<>("send_msg",params);
            System.out.println(JSONObject.toJSONString(request));
            Client.sendMessage(JSONObject.toJSONString(request));
            return true;
        }
        else return false;
    }

    private void match(Message message,String[] rawMessage) throws InterruptedException {
        switch (rawMessage.length){
            case 1 -> {
                System.out.println("case 1");
                Random random = new Random();
                int num = random.nextInt((int) longDataService.count());
                LongData data = longDataService.getById(num + 1);
                params.addImageMessageSegment(data.getUrl());
            }
            case 2 -> {
                params.setMessage_type("group");
                params.setGroup_id(251697087L);
                params.addTextMessageSegment("收到龙图添加请求，请进行审核。");
                request = new Request<>("send_msg",params);
                System.out.println(JSONObject.toJSONString(request));
                Client.sendMessage(JSONObject.toJSONString(request));
                Thread.sleep(3000L);
                params.clearMessage();
                params.setMessage(message.getSegmentList());
                request = new Request<>("send_msg",params);
                System.out.println(JSONObject.toJSONString(request));
                Client.sendMessage(JSONObject.toJSONString(request));
                Thread.sleep(3000L);
                params = new Params(message);
                params.addTextMessageSegment("添加请求已受理，审核通过后就可以召唤你提供的龙图啦QAQ");
            }
            case 3 -> {
                System.out.println("case 3");
                if(params.getGroup_id() == 251697087L){
                    System.out.println("case 3 admin");
                    if(rawMessage[2].contains("true")){
                        MessageData messageData = message.getSegmentList().get(1).getData();
                        if(messageData instanceof ImageData imageData){
                            boolean ret = longDataService.add(imageData.getUrl());
                            if(ret){
                                params.addTextMessageSegment("龙图已加入召唤池！");
                            }
                            else {
                                params.addTextMessageSegment("添加龙图失败：数据库异常");
                            }
                        }
                    }
                }
            }
            default -> params.addTextMessageSegment("参数有误，请输入/help 4查看帮助文档。");
        }
    }
}
