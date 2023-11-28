package com.pygeton.nibot.communication.function;

import com.alibaba.fastjson.JSONObject;
import com.pygeton.nibot.communication.entity.Message;
import com.pygeton.nibot.communication.entity.chuni.ChunithmChartInfo;
import com.pygeton.nibot.communication.entity.params.SendMsgParams;
import com.pygeton.nibot.communication.event.IMessageEvent;
import com.pygeton.nibot.communication.service.ChunithmHttpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class Chunithm extends Function implements IMessageEvent {

    @Autowired
    ChunithmHttpService chunithmHttpService;

    @Override
    public int weight() {
        return 50;
    }

    @Override
    public boolean onMessage(Message message) {
        setRawMessage(message);
        if(rawMessage[0].equals("/chuni")){
            sendMsgParams = new SendMsgParams(message);
            if(rawMessage.length > 1){
                match(message);
                return true;
            }
            else {
                sendMsgParams.addTextMessageSegment("参数有误，请输入/help 7查看帮助文档。");
                sendMessage();
                return false;
            }
        }
        else return false;
    }

    private void match(Message message){
        if(rawMessage[1].equals("b30")){
            generateB30AndR10(message.getUserId());
        }
    }

    private void generateB30AndR10(Long userId){
        Map<String, List<JSONObject>> map = chunithmHttpService.getB30AndR10(userId);
        if(map.containsKey("400")){
            sendMsgParams.addTextMessageSegment("未找到玩家，可能是查分器账号没有绑定qq，详见/help 6。");
        }
        else if(map.containsKey("403")){
            sendMsgParams.addTextMessageSegment("该用户禁止他人访问获取数据=_=");
        }
        else {
            String nickname = map.get("userdata").get(0).getString("nickname");
            String rating = String.format("%.2f",map.get("userdata").get(0).getDoubleValue("rating"));
            List<ChunithmChartInfo> b30List = new ArrayList<>();
            List<ChunithmChartInfo> r10List = new ArrayList<>();
            for(JSONObject object : map.get("b30")){
                b30List.add(object.toJavaObject(ChunithmChartInfo.class));
            }
            for(JSONObject object : map.get("r10")){
                r10List.add(object.toJavaObject(ChunithmChartInfo.class));
            }
            StringBuilder builder = new StringBuilder(nickname + "【Rating:" + rating + "】的B30+R10分数列表如下：\n");
            builder.append("------------B30------------\n");
            appendChartInfo(builder,b30List);
            builder.append("------------R10------------\n");
            appendChartInfo(builder,r10List);
            sendMsgParams.addTextMessageSegment(builder.toString());
        }
        sendMessage();
    }

    private void appendChartInfo(StringBuilder builder,List<ChunithmChartInfo> list){
        int i = 1;
        for(ChunithmChartInfo chartInfo : list){
            builder.append(i).append(". ").append(chartInfo.getTitle()).append(" ").append(chartInfo.getLevelLabel()).append("(").append(chartInfo.getDs()).append(") ").append(chartInfo.getScore()).append(" ra:").append(chartInfo.getRa()).append("\n");
            i++;
        }
    }
}
