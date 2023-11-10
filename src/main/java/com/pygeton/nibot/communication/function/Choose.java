package com.pygeton.nibot.communication.function;

import com.alibaba.fastjson.JSONObject;
import com.pygeton.nibot.communication.entity.Message;
import com.pygeton.nibot.communication.entity.Params;
import com.pygeton.nibot.communication.entity.Request;
import com.pygeton.nibot.communication.event.IMessageEvent;
import com.pygeton.nibot.communication.websocket.Client;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class Choose implements IMessageEvent {

    @Override
    public int weight() {
        return 5;
    }

    @Override
    public boolean onMessage(Message message) {
        String[] rawMessage = message.getRaw_message().split(" ");
        if(rawMessage[0].equals("/choose")){
            Request<Params> request = new Request<>();
            request.setAction("send_msg");
            Params params = new Params();
            params.setUser_id(message.getUser_id());
            params.setGroup_id(message.getGroup_id());
            params.setMessage_type(message.getMessage_type());
            if(rawMessage.length > 1){
                params.setMessage(choose(rawMessage));
            }
            else params.setMessage("没有选项我怎么帮你选呢？");
            params.setAuto_escape(false);
            request.setParams(params);
            System.out.println(JSONObject.toJSONString(request));
            Client.sendMessage(JSONObject.toJSONString(request));
            return true;
        }
        else return false;
    }

    private String choose(String[] rawMessage){
        List<String> list = new ArrayList<>(rawMessage.length - 1);
        list.addAll(Arrays.asList(rawMessage).subList(1, rawMessage.length));
        Random random = new Random();
        int num = random.nextInt(list.size());
        return list.get(num);
    }
}
