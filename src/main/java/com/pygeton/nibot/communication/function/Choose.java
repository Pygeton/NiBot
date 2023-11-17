package com.pygeton.nibot.communication.function;

import com.pygeton.nibot.communication.entity.Message;
import com.pygeton.nibot.communication.entity.params.SendMsgParams;
import com.pygeton.nibot.communication.event.IMessageEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class Choose extends Function implements IMessageEvent {

    @Override
    public int weight() {
        return 10;
    }

    @Override
    public boolean onMessage(Message message) {
        String[] rawMessage = message.getRaw_message().split(" ");
        if(rawMessage[0].equals("/choose")){
            sendMsgParams = new SendMsgParams(message);
            String text;
            if(rawMessage.length > 1){
                text = choose(rawMessage);
            }
            else {
                text = "没有选项我怎么帮你选呢？";
            }
            sendMsgParams.addTextMessageSegment(text);
            sendMessage();
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
