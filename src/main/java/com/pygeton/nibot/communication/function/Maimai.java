package com.pygeton.nibot.communication.function;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pygeton.nibot.communication.entity.Message;
import com.pygeton.nibot.communication.entity.mai.MaimaiChartInfo;
import com.pygeton.nibot.communication.entity.params.SendMsgParams;
import com.pygeton.nibot.communication.event.IMessageEvent;
import com.pygeton.nibot.communication.service.MaimaiHttpService;
import com.pygeton.nibot.repository.entity.MaimaiChartData;
import com.pygeton.nibot.repository.entity.MaimaiSongData;
import com.pygeton.nibot.repository.service.MaimaiChartDataServiceImpl;
import com.pygeton.nibot.repository.service.MaimaiSongDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

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
            //开发者功能
            case "init" -> initDatabase(message.getUserId());
            case "update" -> updateDatabase(message.getUserId());
            case "test" -> test(message.getUserId());
            //常规功能
            case "b50" -> generateB50(message.getUserId());
            case "info" -> getSongInfo();
            case "search" -> searchSong();
            case "add" -> addAilaForSong();
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
            }
        }
        else {
            sendMsgParams.addTextMessageSegment("你没有使用这个命令的权限喵");
        }
        sendMessage();
    }

    private void updateDatabase(Long userId){
        if(userId == 1944539440L){
            boolean allDone = true;
            boolean chartRet = maimaiChartDataService.updateFromJson(maimaiHttpService.getMusicData());
            if(chartRet){
                sendMsgParams.addTextMessageSegment("谱面数据库更新完成！\n");
            }
            else {
                allDone = false;
                sendMsgParams.addTextMessageSegment("谱面数据库更新失败...\n");
            }
            boolean songRet = maimaiSongDataService.updateFromJson(maimaiHttpService.getMusicData());
            if(songRet){
                sendMsgParams.addTextMessageSegment("歌曲数据库更新完成！\n");
            }
            else {
                allDone = false;
                sendMsgParams.addTextMessageSegment("歌曲数据库更新失败...\n");
            }
            boolean coverRet = maimaiSongDataService.updateCoverUrl(maimaiChartDataService.getTitleAndOfficialIdList());
            if(coverRet){
                sendMsgParams.addTextMessageSegment("歌曲封面地址更新完成！\n");
            }
            else {
                allDone = false;
                sendMsgParams.addTextMessageSegment("歌曲封面地址更新失败...\n");
            }
            if(allDone){
                sendMsgParams.addTextMessageSegment("本次数据库更新全部执行完毕！");
            }
            else {
                sendMsgParams.addTextMessageSegment("部分数据库更新操作发生了错误，请检查日志。");
            }
        }
        else {
            sendMsgParams.addTextMessageSegment("你没有使用这个命令的权限喵");
        }
        sendMessage();
    }

    //测试用模块
    private void test(Long userId){
        if(userId == 1944539440L){
            sendMsgParams.addTextMessageSegment("这是一条测试信息喵");
        }
        else sendMsgParams.addTextMessageSegment("你没有使用这个命令的权限喵");
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

    private void getSongInfo(){
        if(rawMessage.length == 3){
            int officialId = Integer.parseInt(rawMessage[2]);
            if(officialId <= 0){
                sendMsgParams.addTextMessageSegment("这不是一个合法歌曲id哦");
            }
            else {
                MaimaiChartData chartData = maimaiChartDataService.getChartData(officialId);
                MaimaiSongData songData = maimaiSongDataService.getSongData(maimaiChartDataService.getTitleKana(officialId));
                if(chartData == null || songData == null){
                    sendMsgParams.addTextMessageSegment("找不到此歌曲呢=_=");
                }
                else {
                    sendMsgParams.addImageMessageSegment("file:///sdcard/Pictures/Maimai/" + songData.getCoverUrl());
                    StringBuilder builder = new StringBuilder("\n");
                    builder.append(officialId).append(".").append(songData.getTitle()).append("(").append(chartData.getType()).append(")\n");
                    builder.append("难度:").append(chartData.getBasicLevel()).append("/").append(chartData.getAdvancedLevel()).append("/").append(chartData.getExpertLevel()).append("/").append(chartData.getMasterLevel());
                    if(chartData.getRemasterLevel() != null){
                        builder.append("/").append(chartData.getRemasterLevel()).append("\n");
                    }
                    else builder.append("\n");
                    appendSongInfo(builder,chartData,songData);
                    sendMsgParams.addTextMessageSegment(builder.toString());
                }
            }
        }
        else if(rawMessage.length == 4){
            int officialId = Integer.parseInt(rawMessage[2]);
            if(officialId <= 0){
                sendMsgParams.addTextMessageSegment("这不是一个合法歌曲id哦");
            }
            else {
                String level = "Unknown";
                int levelIndex;
                switch (rawMessage[3]){
                    case "绿" -> {
                        level = "Basic";
                        levelIndex = 0;
                    }
                    case "黄" -> {
                        level = "Advanced";
                        levelIndex = 1;
                    }
                    case "红" -> {
                        level = "Expert";
                        levelIndex = 2;
                    }
                    case "紫" -> {
                        level = "Master";
                        levelIndex = 3;
                    }
                    case "白" -> {
                        level = "Re:Master";
                        levelIndex = 4;
                    }
                    default -> levelIndex = -1;
                }
                if(levelIndex == -1){
                    sendMsgParams.addTextMessageSegment("参数有误，请输入/help 6查看帮助文档。");
                }
                else {
                    MaimaiChartData chartData = maimaiChartDataService.getChartData(officialId);
                    MaimaiSongData songData = maimaiSongDataService.getSongData(maimaiChartDataService.getTitleKana(officialId));
                    if(chartData == null || songData == null){
                        sendMsgParams.addTextMessageSegment("找不到此歌曲呢=_=");
                    } else if(levelIndex == 4 && chartData.getRemasterLevel() == null){
                        sendMsgParams.addTextMessageSegment("这首歌没有Re:Master难度的谱面哦");
                    } else {
                        sendMsgParams.addImageMessageSegment("file:///sdcard/Pictures/Maimai/" + songData.getCoverUrl());
                        JSONArray dataList = JSON.parseArray(chartData.getDataList());
                        JSONObject data = dataList.getJSONObject(levelIndex);
                        JSONArray notes = data.getJSONArray("notes");
                        int tap = notes.getIntValue(0);
                        int hold = notes.getIntValue(1);
                        int slide = notes.getIntValue(2);
                        int breaks = notes.getIntValue(3);
                        int combo = tap + hold + slide + breaks;
                        int touch = 0;
                        if (notes.size() > 4) {
                            touch = notes.getIntValue(4);
                            combo += touch;
                        }
                        StringBuilder builder = new StringBuilder("\n");
                        builder.append(officialId).append(".").append(songData.getTitle()).append("(").append(chartData.getType()).append(")\n");
                        builder.append("难度:").append(level).append("\n");
                        builder.append("定数:");
                        switch (levelIndex){
                            case 0 -> builder.append(chartData.getBasicConstant()).append("\n");
                            case 1 -> builder.append(chartData.getAdvancedConstant()).append("\n");
                            case 2 -> builder.append(chartData.getExpertConstant()).append("\n");
                            case 3 -> builder.append(chartData.getMasterConstant()).append("\n");
                            case 4 -> builder.append(chartData.getRemasterConstant()).append("\n");
                        }
                        builder.append("物量:").append(combo).append("\n");
                        builder.append("Tap:").append(tap).append("\n");
                        builder.append("Hold:").append(hold).append("\n");
                        builder.append("Slide:").append(slide).append("\n");
                        builder.append("Break:").append(breaks).append("\n");
                        if (notes.size() > 4) {
                            builder.append("Touch:").append(touch).append("\n");
                        }
                        builder.append("谱师:").append(data.getString("charter")).append("\n");
                        appendSongInfo(builder, chartData, songData);
                        sendMsgParams.addTextMessageSegment(builder.toString());
                    }
                }
            }
        }
        else {
            sendMsgParams.addTextMessageSegment("参数有误，请输入/help 6查看帮助文档。");
        }
        sendMessage();
    }

    private void appendSongInfo(StringBuilder builder,MaimaiChartData chartData,MaimaiSongData songData){
        builder.append("曲师:").append(songData.getArtist()).append("\n");
        builder.append("BPM:").append(songData.getBpm()).append("\n");
        builder.append("版本:").append(chartData.getVersion()).append("\n");
        builder.append("分类:").append(songData.getGenre()).append("\n");
        if(songData.getAliasList() != null){
            JSONArray aliasList = JSON.parseArray(songData.getAliasList());
            StringJoiner joiner = new StringJoiner(",");
            for (int i = 0;i < aliasList.size();i++){
                joiner.add(aliasList.getString(i));
            }
            builder.append("别名:").append(joiner).append("\n");
        }
        if(songData.getRemark() != null){
            builder.append("备注:").append(songData.getRemark());
        }
    }

    private void searchSong(){
        if(rawMessage.length == 3) {
            Map<String, String> titleMap = maimaiSongDataService.getTitleMap(rawMessage[2]);
            Map<String, String> resultMap = new TreeMap<>();
            if(titleMap.isEmpty()){
                sendMsgParams.addTextMessageSegment("没有找到任何结果哦，请更换关键字再尝试一下喵");
            }
            else {
                titleMap.forEach((titleKana,title) -> {
                    List<Integer> idList = maimaiChartDataService.getOfficialId(titleKana);
                    String id;
                    if(!idList.contains(null)){
                        if(idList.size() == 1){
                            id = idList.get(0).toString();
                        }
                        else {
                            id = idList.get(0).toString() + "/" + idList.get(1).toString();
                        }
                        resultMap.put(id,title);
                    }
                });
                int count = resultMap.size();
                StringBuilder builder = new StringBuilder("共找到").append(count).append("条可能的结果喵(*≧▽≦)\n");
                resultMap.forEach((id,title) -> builder.append(id).append(".").append(title).append("\n"));
                sendMsgParams.addTextMessageSegment(builder.toString());
            }
        }
        else {
            sendMsgParams.addTextMessageSegment("参数有误，请输入/help 6查看帮助文档。");
        }
        sendMessage();
    }

    private void addAilaForSong(){
        if(rawMessage.length == 4){
            MaimaiSongData songData = maimaiSongDataService.getSongData(maimaiChartDataService.getTitleKana(Integer.parseInt(rawMessage[2])));
            if(songData == null){
                sendMsgParams.addTextMessageSegment("此谱面id没有对应的歌曲哦");
            }
            else {
                String aliasList = songData.getAliasList();
                JSONArray aliasArray;
                if(aliasList != null){
                    aliasArray = JSON.parseArray(songData.getAliasList());
                }
                else aliasArray = new JSONArray();
                if(aliasArray.contains(rawMessage[3])){
                    sendMsgParams.addTextMessageSegment("这个别名已经存在，无需重复添加啦~");
                }
                else {
                    aliasArray.add(rawMessage[3]);
                    songData.setAliasList(aliasArray.toString());
                    if(maimaiSongDataService.saveOrUpdate(songData)){
                        sendMsgParams.addTextMessageSegment("别名添加成功！");
                    }
                    else {
                        sendMsgParams.addTextMessageSegment("别名添加失败，可能是数据库内部发生了异常。");
                    }
                }
            }
        }
        else {
            sendMsgParams.addTextMessageSegment("参数有误，请输入/help 6查看帮助文档。");
        }
        sendMessage();
    }
}
