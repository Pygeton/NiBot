package com.pygeton.nibot.communication.function;

import com.pygeton.nibot.communication.entity.Message;
import com.pygeton.nibot.communication.entity.data.AtData;
import com.pygeton.nibot.communication.entity.data.MessageData;
import com.pygeton.nibot.communication.entity.params.SendMsgParams;
import com.pygeton.nibot.communication.event.IMessageEvent;
import com.pygeton.nibot.repository.service.LuckDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Random;

@Component
public class Luck extends Function implements IMessageEvent {

    @Autowired
    private LuckDataServiceImpl luckDataService;

    @Override
    public int weight() {
        return 100;
    }

    @Override
    public boolean onMessage(Message message) {
        setRawMessage(message);
        if(rawMessage[0].equals("/luck")){
            Random random = new Random();
            int luck = random.nextInt(101);
            LocalDate date = LocalDate.now();
            LocalDate fes = LocalDate.of(2024,4,1);
            if(date.equals(fes)){
                luck = 1007499;
            }
            String text;
            sendMsgParams = new SendMsgParams(message);
            if(rawMessage.length == 1){
                boolean ret = luckDataService.saveOrUpdateLuck(message.getUserId(),luck);
                if(!ret){
                    luck = luckDataService.getLuck(message.getUserId());
                }
                if(message.getMessageType().equals("group")){
                    if(!message.getSender().getCard().equals("")){
                        text = message.getSender().getCard() + "的今日运势为：" + luck + " 【" + getLuckText(luck) + "】";
                    }
                    else {
                        text = message.getSender().getNickname() + "的今日运势为：" + luck + " 【" + getLuckText(luck) + "】";
                    }
                }
                else{
                    text = message.getUserId() + "的今日运势为：" + luck + " 【" + getLuckText(luck) + "】";
                }
                sendMsgParams.addTextMessageSegment(text);
            }
            else if(rawMessage.length == 2){
                if(message.getMessageType().equals("group")){
                    MessageData messageData = message.getSegmentList().get(1).getData();
                    if(messageData instanceof AtData atData){
                        boolean ret = luckDataService.saveOrUpdateLuck(atData.getQq(), luck);
                        if(!ret){
                            luck = luckDataService.getLuck(atData.getQq());
                        }
                        text = atData.getQq() + "的今日运势为：" + luck + " 【" + getLuckText(luck) + "】";
                        sendMsgParams.addTextMessageSegment(text);
                    }
                    else sendMsgParams.addTextMessageSegment("参数有误，请输入/help 1查看帮助文档>_<");
                }
                else sendMsgParams.addTextMessageSegment("这个功能只有在群聊里才能使用哦QAQ");
            }
            else sendMsgParams.addTextMessageSegment("参数有误，请输入/help 1查看帮助文档>_<");
            sendMessage();
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
