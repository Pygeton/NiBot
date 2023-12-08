package com.pygeton.nibot.communication.function;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pygeton.nibot.communication.entity.Message;
import com.pygeton.nibot.communication.entity.chuni.ChunithmChartInfo;
import com.pygeton.nibot.communication.entity.chuni.ChunithmDifficulty;
import com.pygeton.nibot.communication.entity.params.SendMsgParams;
import com.pygeton.nibot.communication.event.IMessageEvent;
import com.pygeton.nibot.communication.service.ChunithmHttpService;
import com.pygeton.nibot.repository.entity.ChunithmData;
import com.pygeton.nibot.repository.service.ChunithmDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

@Component
public class Chunithm extends Function implements IMessageEvent {

    @Autowired
    ChunithmDataServiceImpl chunithmDataService;

    @Autowired
    ChunithmHttpService chunithmHttpService;

    @Override
    public int weight() {
        return 50;
    }

    @Override
    public boolean onMessage(Message message) {
        setRawMessage(message);
        if(rawMessage[0].equals("/chu")){
            sendMsgParams = new SendMsgParams(message);
            if(rawMessage.length > 1){
                match(message);
                return true;
            }
            else {
                sendMsgParams.addTextMessageSegment("参数有误，请输入/help 7查看帮助文档>_<");
                sendMessage();
                return false;
            }
        }
        else return false;
    }

    private void match(Message message){
        switch (rawMessage[1]){
            //开发者功能
            case "update" -> updateDatabase(message.getUserId());
            //常规功能
            case "b30" -> generateB30AndR10(message.getUserId());
            case "info" -> getSongInfo();
            case "search" -> searchSong();
            case "add" -> addAilaForSong();
        }
    }

    private void updateDatabase(Long userId){
        if(userId == 1944539440L){
            if(rawMessage.length == 3){
                switch (rawMessage[2]){
                    case "sega" -> {
                        boolean ret = chunithmDataService.updateFromSega(chunithmHttpService.getMusicDataFromSega());
                        if(ret){
                            sendMsgParams.addTextMessageSegment("歌曲数据库(日服)更新完成！");
                        }
                        else {
                            sendMsgParams.addTextMessageSegment("歌曲数据库(日服)更新失败...");
                        }
                    }
                    case "fish" -> {
                        boolean ret = chunithmDataService.updateFromDivingFish(chunithmHttpService.getMusicDataFromDivingFish());
                        if(ret){
                            sendMsgParams.addTextMessageSegment("歌曲数据库(国服)更新完成！");
                        }
                        else {
                            sendMsgParams.addTextMessageSegment("歌曲数据库(国服)更新失败...");
                        }
                    }
                    case "cover" -> {
                        boolean ret = chunithmDataService.updateCoverUrl();
                        if(ret){
                            sendMsgParams.addTextMessageSegment("封面更新完成！");
                        }
                        else {
                            sendMsgParams.addTextMessageSegment("封面更新失败...");
                        }
                    }
                }
            }
            else {
                sendMsgParams.addTextMessageSegment("参数有误，请输入/help 7查看帮助文档>_<");
            }
        }
        else {
            sendMsgParams.addTextMessageSegment("你没有使用这个命令的权限喵");
        }
        sendMessage();
    }

    private void generateB30AndR10(Long userId){
        Map<String, List<JSONObject>> map = chunithmHttpService.getB30AndR10(userId);
        if(map.containsKey("400")){
            sendMsgParams.addTextMessageSegment("未找到玩家，可能是查分器账号没有绑定qq，详见/help 7>_<");
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

    private void getSongInfo(){
        if(rawMessage.length == 3){
            int officialId = Integer.parseInt(rawMessage[2]);
            if(officialId <= 0){
                sendMsgParams.addTextMessageSegment("这不是一个合法歌曲id哦>_<");
            }
            else {
                ChunithmData data = chunithmDataService.getChunithmData(officialId);
                if(data == null){
                    sendMsgParams.addTextMessageSegment("找不到此歌曲呢=_=");
                }
                else {
                    //迁移代码原位，待WE谱面封面完善再回迁
                    StringBuilder builder = new StringBuilder();
                    builder.append(officialId).append(".").append(data.getTitle());
                    if(officialId / 1000 == 8){
                        builder.append("[").append(data.getWeType()).append("]\n");
                        builder.append("难度:").append(data.getWeStar()).append("⭐\n");
                        JSONArray dataList = JSON.parseArray(data.getDataList());
                        JSONObject dataObject = dataList.getJSONObject(5);
                        builder.append("物量:").append(dataObject.getString("combo")).append("\n");
                        builder.append("谱师:").append(dataObject.getString("charter")).append("\n");
                    }
                    else {
                        builder.append("\n");
                        builder.append("难度:").append(data.getBasicLevel()).append("/").append(data.getAdvancedLevel()).append("/").append(data.getExpertLevel()).append("/").append(data.getMasterLevel());
                        if(data.getUltimaLevel() != null){
                            builder.append("/").append(data.getUltimaLevel()).append("\n");
                        }
                        else builder.append("\n");
                        //此代码位置暂时迁移
                        sendMsgParams.addImageMessageSegment("file:///sdcard/Pictures/Chunithm/" + data.getCoverUrl());
                    }
                    appendSongInfo(builder,data);
                    sendMsgParams.addTextMessageSegment(builder.toString());
                }
            }
        }
        else if(rawMessage.length == 4){
            int officialId = Integer.parseInt(rawMessage[2]);
            if(officialId <= 0){
                sendMsgParams.addTextMessageSegment("这不是一个合法歌曲id哦>_<");
            }
            else if(officialId / 1000 == 8){
                sendMsgParams.addImageMessageSegment("这是一个WORLD'S END谱面，无需输入难度参数>_<");
            }
            else {
                ChunithmDifficulty difficulty = new ChunithmDifficulty(rawMessage[3]);
                if(difficulty.getIndex() == -1){
                    sendMsgParams.addTextMessageSegment("参数有误，请输入/help 6查看帮助文档>_<");
                }
                else {
                    ChunithmData data = chunithmDataService.getChunithmData(officialId);
                    if(data == null){
                        sendMsgParams.addTextMessageSegment("找不到此歌曲呢=_=");
                    } else if (!data.getIsEnabled()){
                        sendMsgParams.addTextMessageSegment("这是一首国服未实装歌曲，没有详细信息可供查看哦>_<");
                    } else if (difficulty.getIndex() == 4 && data.getUltimaLevel() == null){
                        sendMsgParams.addTextMessageSegment("这首歌没有Ultima难度的谱面哦");
                    } else {
                        sendMsgParams.addImageMessageSegment("file:///sdcard/Pictures/Chunithm/" + data.getCoverUrl());
                        JSONArray dataList = JSON.parseArray(data.getDataList());
                        JSONObject dataObject = dataList.getJSONObject(difficulty.getIndex());
                        StringBuilder builder = new StringBuilder("\n");
                        builder.append(officialId).append(".").append(data.getTitle()).append("\n");
                        builder.append("难度:").append(difficulty.getDifficulty()).append("\n");
                        builder.append("定数:");
                        switch (difficulty.getIndex()){
                            case 0 -> builder.append(data.getBasicConstant()).append("\n");
                            case 1 -> builder.append(data.getAdvancedConstant()).append("\n");
                            case 2 -> builder.append(data.getExpertConstant()).append("\n");
                            case 3 -> builder.append(data.getMasterConstant()).append("\n");
                            case 4 -> builder.append(data.getUltimaConstant()).append("\n");
                        }
                        builder.append("物量:").append(dataObject.getString("combo")).append("\n");
                        builder.append("谱师:").append(dataObject.getString("charter")).append("\n");
                        appendSongInfo(builder,data);
                        sendMsgParams.addTextMessageSegment(builder.toString());
                    }
                }
            }
        }
        else {
            sendMsgParams.addTextMessageSegment("参数有误，请输入/help 7查看帮助文档>_<");
        }
        sendMessage();
    }

    private void appendSongInfo(StringBuilder builder, ChunithmData data){
        builder.append("曲师:").append(data.getArtist()).append("\n");
        if(data.getIsEnabled()){
            builder.append("BPM:").append(data.getBpm()).append("\n");
            builder.append("版本:").append(data.getVersion()).append("\n");
        }
        builder.append("分类:").append(data.getGenre()).append("\n");
        if(data.getAliasList() != null){
            JSONArray aliasList = JSON.parseArray(data.getAliasList());
            StringJoiner joiner = new StringJoiner(",");
            for (int i = 0;i < aliasList.size();i++){
                joiner.add(aliasList.getString(i));
            }
            builder.append("别名:").append(joiner).append("\n");
        }
    }

    private void searchSong(){
        if(rawMessage.length == 3){
            Map<Integer, String> resultMap = chunithmDataService.getResultMap(rawMessage[2]);
            if(resultMap.isEmpty()){
                sendMsgParams.addTextMessageSegment("没有找到任何结果哦，请更换关键字再尝试一下喵");
            }
            else {
                int count = resultMap.size();
                StringBuilder builder = new StringBuilder("共找到").append(count).append("条可能的结果喵(*≧▽≦)\n");
                builder.append("注意：标记为(*)的为国服未实装歌曲\n");
                resultMap.forEach((id,title) -> builder.append(id).append(".").append(title).append("\n"));
                sendMsgParams.addTextMessageSegment(builder.toString());
            }
        }
        else {
            sendMsgParams.addTextMessageSegment("参数有误，请输入/help 7查看帮助文档>_<");
        }
        sendMessage();
    }

    private void addAilaForSong(){
        if(rawMessage.length == 4){
            ChunithmData data = chunithmDataService.getChunithmData(Integer.parseInt(rawMessage[2]));
            if(data == null){
                sendMsgParams.addTextMessageSegment("此谱面id没有对应的歌曲哦>_<");
            }
            else {
                String aliasList = data.getAliasList();
                JSONArray aliasArray;
                if(aliasList != null){
                    aliasArray = JSON.parseArray(data.getAliasList());
                }
                else aliasArray = new JSONArray();
                if(aliasArray.contains(rawMessage[3])){
                    sendMsgParams.addTextMessageSegment("这个别名已经存在，无需重复添加啦~");
                }
                else {
                    aliasArray.add(rawMessage[3]);
                    data.setAliasList(aliasArray.toString());
                    if(chunithmDataService.saveOrUpdate(data)){
                        sendMsgParams.addTextMessageSegment("别名添加成功！");
                    }
                    else {
                        sendMsgParams.addTextMessageSegment("别名添加失败，可能是数据库内部发生了异常>_<");
                    }
                }
            }
        }
        else {
            sendMsgParams.addTextMessageSegment("参数有误，请输入/help 6查看帮助文档>_<");
        }
        sendMessage();
    }
}
