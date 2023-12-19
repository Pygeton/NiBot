package com.pygeton.nibot.communication.function;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pygeton.nibot.communication.entity.Message;
import com.pygeton.nibot.communication.entity.mai.MaimaiChartInfo;
import com.pygeton.nibot.communication.entity.mai.MaimaiDifficulty;
import com.pygeton.nibot.communication.entity.mai.MaimaiNoteInfo;
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
        return 90;
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
            case "line" -> calculateScoreLine();
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
            switch (rawMessage[2]){
                case "chart" -> {
                    boolean chartRet = maimaiChartDataService.updateFromJson(maimaiHttpService.getMusicData());
                    if(chartRet){
                        sendMsgParams.addTextMessageSegment("谱面数据库更新完成！\n");
                    }
                    else {
                        sendMsgParams.addTextMessageSegment("谱面数据库更新失败...\n");
                    }
                }
                case "song" -> {
                    boolean songRet = maimaiSongDataService.updateFromJson(maimaiHttpService.getMusicData());
                    if(songRet){
                        sendMsgParams.addTextMessageSegment("歌曲数据库更新完成！\n");
                    }
                    else {
                        sendMsgParams.addTextMessageSegment("歌曲数据库更新失败...\n");
                    }
                }
                case "cover" -> {
                    boolean coverRet = maimaiSongDataService.updateCoverUrl(maimaiChartDataService.getTitleAndOfficialIdList());
                    if(coverRet){
                        sendMsgParams.addTextMessageSegment("歌曲封面地址更新完成！\n");
                    }
                    else {
                        sendMsgParams.addTextMessageSegment("歌曲封面地址更新失败...\n");
                    }
                }
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
            sendMsgParams.addTextMessageSegment("未找到玩家，可能是查分器账号没有绑定qq，详见/help 6>_<");
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
            builder.append(i).append(".").append(chartInfo.getTitle()).append("(").append(chartInfo.getType()).append(") ").append(chartInfo.getLevelLabel()).append("(").append(chartInfo.getDs()).append(") ").append(String.format("%.4f", chartInfo.getAchievements())).append("% ra:").append(chartInfo.getRa()).append("\n");
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
                MaimaiChartData chartData = maimaiChartDataService.getChartData(officialId);
                MaimaiSongData songData = maimaiSongDataService.getSongData(officialId);
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
                sendMsgParams.addTextMessageSegment("这不是一个合法歌曲id哦>_<");
            }
            else {
                MaimaiDifficulty difficulty = new MaimaiDifficulty(rawMessage[3]);
                if(difficulty.getIndex() == -1){
                    sendMsgParams.addTextMessageSegment("参数有误，请输入/help 6查看帮助文档>_<");
                }
                else {
                    MaimaiChartData chartData = maimaiChartDataService.getChartData(officialId);
                    MaimaiSongData songData = maimaiSongDataService.getSongData(officialId);
                    if(chartData == null || songData == null){
                        sendMsgParams.addTextMessageSegment("找不到此歌曲呢=_=");
                    } else if (difficulty.getIndex() == 4 && chartData.getRemasterLevel() == null){
                        sendMsgParams.addTextMessageSegment("这首歌没有Re:Master难度的谱面哦");
                    } else {
                        sendMsgParams.addImageMessageSegment("file:///sdcard/Pictures/Maimai/" + songData.getCoverUrl());
                        MaimaiNoteInfo noteInfo = new MaimaiNoteInfo(chartData.getDataList(),difficulty.getIndex());
                        StringBuilder builder = new StringBuilder("\n");
                        builder.append(officialId).append(".").append(songData.getTitle()).append("(").append(chartData.getType()).append(")\n");
                        builder.append("难度:").append(difficulty.getDifficulty()).append("\n");
                        builder.append("定数:");
                        switch (difficulty.getIndex()){
                            case 0 -> builder.append(chartData.getBasicConstant()).append("\n");
                            case 1 -> builder.append(chartData.getAdvancedConstant()).append("\n");
                            case 2 -> builder.append(chartData.getExpertConstant()).append("\n");
                            case 3 -> builder.append(chartData.getMasterConstant()).append("\n");
                            case 4 -> builder.append(chartData.getRemasterConstant()).append("\n");
                        }
                        builder.append("物量:").append(noteInfo.getFullCombo()).append("\n");
                        builder.append("Tap:").append(noteInfo.getTap()).append("\n");
                        builder.append("Hold:").append(noteInfo.getHold()).append("\n");
                        builder.append("Slide:").append(noteInfo.getSlide()).append("\n");
                        builder.append("Break:").append(noteInfo.getBreaks()).append("\n");
                        if (noteInfo.getTouch() > 0) {
                            builder.append("Touch:").append(noteInfo.getTouch()).append("\n");
                        }
                        builder.append("谱师:").append(JSON.parseArray(chartData.getDataList()).getJSONObject(difficulty.getIndex()).getString("charter")).append("\n");
                        appendSongInfo(builder,chartData,songData);
                        sendMsgParams.addTextMessageSegment(builder.toString());
                    }
                }
            }
        }
        else {
            sendMsgParams.addTextMessageSegment("参数有误，请输入/help 6查看帮助文档>_<");
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
                    String officialId;
                    List<Integer> validIdList = new ArrayList<>();
                    for(Integer id : idList){
                        if(id != 0){
                            validIdList.add(id);
                        }
                    }
                    if(validIdList.size() == 1){
                        officialId = validIdList.get(0).toString();
                        resultMap.put(officialId,title);
                    }
                    else if(validIdList.size() == 2){
                        officialId = validIdList.get(0).toString() + "/" + idList.get(1).toString();
                        resultMap.put(officialId,title);
                    }
                });
                int count = resultMap.size();
                StringBuilder builder = new StringBuilder("共找到").append(count).append("条可能的结果喵(*≧▽≦)\n");
                resultMap.forEach((id,title) -> builder.append(id).append(".").append(title).append("\n"));
                sendMsgParams.addTextMessageSegment(builder.toString());
            }
        }
        else {
            sendMsgParams.addTextMessageSegment("参数有误，请输入/help 6查看帮助文档>_<");
        }
        sendMessage();
    }

    private void addAilaForSong(){
        if(rawMessage.length == 4){
            MaimaiSongData songData = maimaiSongDataService.getSongData(Integer.parseInt(rawMessage[2]));
            if(songData == null){
                sendMsgParams.addTextMessageSegment("此谱面id没有对应的歌曲哦>_<");
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

    private void calculateScoreLine(){
        if(rawMessage.length == 4 || rawMessage.length == 5){
            int officialId = Integer.parseInt(rawMessage[2]);
            MaimaiDifficulty difficulty = new MaimaiDifficulty(rawMessage[3]);
            try {
                MaimaiChartData chartData = maimaiChartDataService.getChartData(officialId);
                MaimaiSongData songData = maimaiSongDataService.getSongData(officialId);
                MaimaiNoteInfo noteInfo = new MaimaiNoteInfo(chartData.getDataList(),difficulty.getIndex());
                //后续可能考虑图形化
                StringBuilder builder = new StringBuilder(rawMessage[2]).append(".").append(songData.getTitle());
                builder.append("(").append(chartData.getType()).append(")的").append(difficulty.getDifficulty()).append("难度的误差列表如下：\n");
                builder.append("种类/Great/Good/Miss\n");
                builder.append("Tap/Touch:").append(String.format("%.4f", -noteInfo.getDeductions()[0][0])).append("%/").append(String.format("%.4f", -noteInfo.getDeductions()[0][1])).append("%/").append(String.format("%.4f", -noteInfo.getDeductions()[0][2])).append("%\n");
                builder.append("Hold:").append(String.format("%.4f", -noteInfo.getDeductions()[1][0])).append("%/").append(String.format("%.4f", -noteInfo.getDeductions()[1][1])).append("%/").append(String.format("%.4f", -noteInfo.getDeductions()[1][2])).append("%\n");
                builder.append("Slide:").append(String.format("%.4f", -noteInfo.getDeductions()[2][0])).append("%/").append(String.format("%.4f", -noteInfo.getDeductions()[2][1])).append("%/").append(String.format("%.4f", -noteInfo.getDeductions()[2][2])).append("%\n");
                builder.append("=====================\n");
                builder.append("Break(Perfect):").append(String.format("%.4f", -noteInfo.getDeductions()[3][0])).append("%(50落)/").append(String.format("%.4f", -noteInfo.getDeductions()[3][1])).append("%(100落)\n");
                builder.append("Break(Great):").append(String.format("%.4f", -noteInfo.getDeductions()[4][0])).append("%/").append(String.format("%.4f", -noteInfo.getDeductions()[4][1])).append("%/").append(String.format("%.4f", -noteInfo.getDeductions()[4][2])).append("%\n");
                builder.append("Break(Good):").append(String.format("%.4f", -noteInfo.getDeductions()[5][0])).append("%\n");
                builder.append("Break(Miss):").append(String.format("%.4f", -noteInfo.getDeductions()[6][0])).append("%\n");
                if(rawMessage.length == 5){
                    double target = Double.parseDouble(rawMessage[4]);
                    double[] bound = noteInfo.getBreakGreatEquivalenceBound();
                    builder.append("=====================\n");
                    builder.append("达到目标").append(target).append("%允许的最大Tap Great数量为").append((int) Math.floor(noteInfo.getFaultTolerance(target))).append("个\n");
                    builder.append("Break 50落等价于").append(String.format("%.2f",noteInfo.getBreak50Equivalence())).append("个Tap Great\n");
                    builder.append("Break Great相当于").append(String.format("%.2f",bound[0])).append("~").append(String.format("%.2f",bound[1])).append("个Tap Great");
                }
                sendMsgParams.addTextMessageSegment(builder.toString());
            }
            catch (IndexOutOfBoundsException e){
                e.printStackTrace();
                sendMsgParams.addTextMessageSegment("参数有误，请输入/help 6查看帮助文档>_<");
            }
            catch (NullPointerException e){
                e.printStackTrace();
                sendMsgParams.addTextMessageSegment("歌曲id有误，可能是歌曲不存在或国服未实装，无法获取数据进行计算>_<");
            }
        }
        else {
            sendMsgParams.addTextMessageSegment("参数有误，请输入/help 6查看帮助文档>_<");
        }
        sendMessage();
    }
}
