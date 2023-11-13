package com.pygeton.nibot.communication.function;

import com.alibaba.fastjson.JSONObject;
import com.pygeton.nibot.communication.entity.Message;
import com.pygeton.nibot.communication.entity.Params;
import com.pygeton.nibot.communication.entity.Request;
import com.pygeton.nibot.communication.event.IMessageEvent;
import com.pygeton.nibot.communication.websocket.Client;
import com.pygeton.nibot.repository.service.LuckDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class Luck implements IMessageEvent {

    Request<Params> request;
    Params params;

    @Autowired
    private LuckDataServiceImpl luckDataService;

    @Override
    public int weight() {
        return 99;
    }

    @Override
    public boolean onMessage(Message message) {
        String[] rawMessage = message.getRaw_message().split(" ");
        if(rawMessage[0].equals("/luck")){
            Random random = new Random();
            int luck = random.nextInt(101);
            boolean ret = luckDataService.saveOrUpdateLuck(message.getUser_id(),luck);
            if(!ret){
                luck = luckDataService.getLuck(message.getUser_id());
            }
            params = new Params(message);
            String text;
            if(message.getMessage_type().equals("group")){
                if(!message.getSender().getCard().equals("")){
                    text = message.getSender().getCard() + "的今日运势为：" + luck + " 【" + getLuckText(luck) + "】";
                }
                else {
                    text = message.getSender().getNickname() + "的今日运势为：" + luck + " 【" + getLuckText(luck) + "】";
                }
            }
            else{
                text = message.getUser_id() + "的今日运势为：" + luck + " 【" + getLuckText(luck) + "】";
            }
            params.addTextMessageSegment(text);
            request = new Request<>("send_msg", params);
            System.out.println(JSONObject.toJSONString(request));
            Client.sendMessage(JSONObject.toJSONString(request));
            return true;
        }
        else return false;
    }

    private String getLuckText(int luck){
        if(luck == 0){
           return "超级大凶";
        }
        else if(luck < 20){
            return "大凶";
        }
        else if(luck < 40){
            return "凶";
        }
        else if(luck < 60){
            return "小吉";
        }
        else if(luck < 80){
            return "吉";
        }
        else if(luck < 100){
            return "大吉";
        }
        else return "超级大吉";
    }
}
