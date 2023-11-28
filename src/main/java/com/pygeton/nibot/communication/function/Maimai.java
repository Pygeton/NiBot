package com.pygeton.nibot.communication.function;

import com.alibaba.fastjson.JSONObject;
import com.pygeton.nibot.communication.entity.Message;
import com.pygeton.nibot.communication.entity.mai.MaimaiChartInfo;
import com.pygeton.nibot.communication.entity.params.SendMsgParams;
import com.pygeton.nibot.communication.event.IMessageEvent;
import com.pygeton.nibot.communication.service.MaimaiHttpService;
import com.pygeton.nibot.repository.service.MaimaiChartDataServiceImpl;
import com.pygeton.nibot.repository.service.MaimaiSongDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class Maimai extends Function implements IMessageEvent {

    @Autowired
    MaimaiSongDataServiceImpl maimaiSongDataService;

    @Autowired
    MaimaiChartDataServiceImpl maimaiChartDataService;

    @Autowired
    MaimaiHttpService maimaiHttpService;

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
            case "init" -> initDatabase(message.getUserId());
            case "b50" -> generateB50(message.getUserId());
        }
    }

    private void initDatabase(Long userId){
        if(userId == 1944539440L){
            switch (rawMessage[2]){
                case "song" -> {
                    boolean ret = maimaiSongDataService.init();
                    if(ret){
                        sendMsgParams.addTextMessageSegment("歌曲数据库初始化完成！");
                    }
                    else {
                        sendMsgParams.addTextMessageSegment("歌曲数据库初始化失败，请检查日志。");
                    }
                }
                case "chart" -> {
                    boolean ret = maimaiChartDataService.init();
                    if(ret){
                        sendMsgParams.addTextMessageSegment("谱面数据库初始化完成！");
                    }
                    else {
                        sendMsgParams.addTextMessageSegment("谱面数据库初始化失败，请检查日志。");
                    }
                }
                case "cover" -> {
                    boolean ret = maimaiSongDataService.updateCoverUrl(maimaiChartDataService.getTitleAndOfficialIdList());
                    if(ret){
                        sendMsgParams.addTextMessageSegment("歌曲封面地址更新完成！");
                    }
                    else {
                        sendMsgParams.addTextMessageSegment("歌曲封面地址更新失败，请检查日志。");
                    }
                }
            }
        }
        else {
            sendMsgParams.addTextMessageSegment("你没有使用这个命令的权限喵");
        }
        sendMessage();
    }

    private void generateB50(Long userId){
        Map<String, List<JSONObject>> map = maimaiHttpService.getB50(userId);
        if(map.containsKey("400")){
            sendMsgParams.addTextMessageSegment("未找到玩家，可能是查分器账号没有绑定qq，详见/help 6。");
        }
        else if(map.containsKey("403")){
            sendMsgParams.addTextMessageSegment("该用户禁止他人访问获取数据=_=");
        }
        else {
            String nickname = map.get("userdata").get(0).getString("nickname");
            int rating = map.get("userdata").get(0).getIntValue("rating");
            List<MaimaiChartInfo> b35List = new ArrayList<>();
            List<MaimaiChartInfo> b15List = new ArrayList<>();
            for(JSONObject object : map.get("sd")){
                b35List.add(object.toJavaObject(MaimaiChartInfo.class));
            }
            for(JSONObject object : map.get("dx")){
                b15List.add(object.toJavaObject(MaimaiChartInfo.class));
            }
            StringBuilder builder = new StringBuilder(nickname + "【Rating:" + rating + "】的B50分数列表如下：\n");
            builder.append("------------B35------------\n");
            appendChartInfo(builder,b35List);
            builder.append("------------B15------------\n");
            appendChartInfo(builder,b15List);
            sendMsgParams.addTextMessageSegment(builder.toString());
        }
        sendMessage();
    }

    private void appendChartInfo(StringBuilder builder,List<MaimaiChartInfo> list){
        int i = 1;
        for(MaimaiChartInfo chartInfo : list){
            builder.append(i).append(". ").append(chartInfo.getTitle()).append("(").append(chartInfo.getType()).append(") ").append(chartInfo.getLevelLabel()).append("(").append(chartInfo.getDs()).append(") ").append(String.format("%.4f", chartInfo.getAchievements())).append("% ra:").append(chartInfo.getRa()).append("\n");
            i++;
        }
    }
}
