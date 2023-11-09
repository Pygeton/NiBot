package com.pygeton.nibot.function;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pygeton.nibot.entity.Message;
import com.pygeton.nibot.entity.Params;
import com.pygeton.nibot.entity.Request;
import com.pygeton.nibot.event.IMessageEvent;
import com.pygeton.nibot.websocket.Client;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class Luck implements IMessageEvent {

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

            Request<Params> request = new Request<>();
            request.setAction("send_msg");
            Params params = new Params();
            params.setUser_id(message.getUser_id());
            params.setGroup_id(message.getGroup_id());
            params.setMessage_type(message.getMessage_type());
            if(message.getMessage_type().equals("group")){
                params.setMessage(message.getSender().getCard() + "的今日运势为：" + luck + " 【" + match(luck) + "】");
            }
            else{
                params.setMessage(message.getUser_id() + "的今日运势为：" + luck + " 【" + match(luck) + "】");
            }
            params.setAuto_escape(false);
            request.setParams(params);
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
