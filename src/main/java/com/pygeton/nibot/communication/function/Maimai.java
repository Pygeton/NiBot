package com.pygeton.nibot.communication.function;

import com.pygeton.nibot.communication.entity.Message;
import com.pygeton.nibot.communication.entity.params.SendMsgParams;
import com.pygeton.nibot.communication.event.IMessageEvent;
import org.springframework.stereotype.Component;

@Component
public class Maimai extends Function implements IMessageEvent {

    @Override
    public int weight() {
        return 70;
    }

    @Override
    public boolean onMessage(Message message) {
        setRawMessage(message);
        if(rawMessage[0].equals("/mai")){
            sendMsgParams = new SendMsgParams(message);
            if(rawMessage.length > 1){
                match(message);
                return true;
            }
            else {
                sendMsgParams.addTextMessageSegment("参数有误，请输入/help 6查看帮助文档。");
                sendMessage();
                return false;
            }
        }
        else return false;
    }

    private void match(Message message){
        switch (rawMessage[1]){
            case "init" -> {
                if(message.getUser_id() == 1944539440L){
                    initializeDatabase();
                }
                else {
                    sendMsgParams.addTextMessageSegment("你没有使用这个命令的权限喵");
                    sendMessage();
                }
            }
        }
    }

    private void initializeDatabase(){

    }
}
