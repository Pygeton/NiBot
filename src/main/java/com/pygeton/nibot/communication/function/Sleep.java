package com.pygeton.nibot.communication.function;

import com.pygeton.nibot.communication.entity.Message;
import com.pygeton.nibot.communication.entity.Response;
import com.pygeton.nibot.communication.entity.params.SendMsgParams;
import com.pygeton.nibot.communication.entity.params.SetGroupBanParams;
import com.pygeton.nibot.communication.event.IMessageEvent;
import com.pygeton.nibot.communication.event.IResponseHandler;
import org.springframework.stereotype.Component;

@Component
public class Sleep extends Function implements IMessageEvent, IResponseHandler {

    @Override
    public int weight() {
        return 95;
    }

    @Override
    public boolean onMessage(Message message) {
        rawMessage = message.getRaw_message().split(" ");
        if(rawMessage[0].equals("/sleep")){
            if(rawMessage.length == 2){
                sendMsgParams = new SendMsgParams(message);
                if(message.getSender().getRole().equals("owner") || message.getSender().getRole().equals("admin")){
                    sendMsgParams.addTextMessageSegment("狗管理自己睡去，衮");
                    sendMessage();
                }
                else {
                    long time = Long.parseLong(rawMessage[1]) * 3600;
                    setGroupBanParams = new SetGroupBanParams(message.getGroup_id(), message.getUser_id(), time);
                    setGroupBan(this);
                }
            }
            return true;
        }
        else return false;
    }

    @Override
    public void handle(Response response) {
        sendMsgParams.addTextMessageSegment("精致睡眠" + rawMessage[1] + "小时，晚安~" );
        sendMessage();
    }

    @Override
    public void timeout() {
        sendMsgParams.addTextMessageSegment("操作失败：响应超时");
        sendMessage();
    }
}
