package com.pygeton.nibot.communication.function;

import com.alibaba.fastjson.JSONObject;
import com.pygeton.nibot.communication.entity.Message;
import com.pygeton.nibot.communication.entity.Params;
import com.pygeton.nibot.communication.entity.Request;
import com.pygeton.nibot.communication.event.IMessageEvent;
import com.pygeton.nibot.communication.websocket.Client;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class Luck implements IMessageEvent {

    Request<Params> request;
    Params params;

    @Override
    public int weight() {
        return 10;
    }

    @Override
    public boolean onMessage(Message message) {
        if(message.getRaw_message().equals("/luck")){
            //同日请求检测，后续补全
            Random random = new Random();
            int luck = random.nextInt(101);
            params = new Params(message);
            String text;
            if(message.getMessage_type().equals("group")){
                text = message.getSender().getCard() + "的今日运势为：" + luck + " 【" + match(luck) + "】";
            }
            else{
                text = message.getUser_id() + "的今日运势为：" + luck + " 【" + match(luck) + "】";
            }
            params.addTextMessageSegment(text);
            request = new Request<>("send_msg", params);
            System.out.println(JSONObject.toJSONString(request));
            Client.sendMessage(JSONObject.toJSONString(request));
            return true;
        }
        else return false;
    }

    private String match(int luck){
        if(luck<20){
            return "大凶";
        }
        else if(luck<40){
            return "凶";
        }
        else if(luck<60){
            return "小吉";
        }
        else if(luck<80){
            return "吉";
        }
        else return "大吉";
    }
}
