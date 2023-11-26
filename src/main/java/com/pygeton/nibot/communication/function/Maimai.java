package com.pygeton.nibot.communication.function;

import com.pygeton.nibot.communication.entity.Message;
import com.pygeton.nibot.communication.entity.params.SendMsgParams;
import com.pygeton.nibot.communication.event.IMessageEvent;
import com.pygeton.nibot.repository.service.MaimaiChartDataServiceImpl;
import com.pygeton.nibot.repository.service.MaimaiSongDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Maimai extends Function implements IMessageEvent {

    @Autowired
    MaimaiSongDataServiceImpl maimaiSongDataService;

    @Autowired
    MaimaiChartDataServiceImpl maimaiChartDataService;

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

    private void initDatabase(Long id){
        if(id == 1944539440L){
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

    private void generateB50(Long id){

    }
}
