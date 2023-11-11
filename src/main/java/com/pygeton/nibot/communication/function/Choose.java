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

    String[] rawMessage;
    Request<Params> request;
    Params params;

    @Override
    public int weight() {
        return 5;
    }

    @Override
    public boolean onMessage(Message message) {
        rawMessage = message.getRaw_message().split(" ");
        if(rawMessage[0].equals("/choose")){
            params = new Params(message);
            String text;
            if(rawMessage.length > 1){
                text = choose();
            }
            else {
                text = "没有选项我怎么帮你选呢？";
            }
            params.addTextMessageSegment(text);
            request = new Request<>("send_msg", params);
            System.out.println(JSONObject.toJSONString(request));
            Client.sendMessage(JSONObject.toJSONString(request));
            return true;
        }
        else return false;
    }

    private String choose(){
        List<String> list = new ArrayList<>(rawMessage.length - 1);
        list.addAll(Arrays.asList(rawMessage).subList(1, rawMessage.length));
        Random random = new Random();
        int num = random.nextInt(list.size());
        return list.get(num);
    }
}
