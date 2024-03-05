package com.pygeton.nibot.communication.function;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pygeton.nibot.communication.entity.Message;
import com.pygeton.nibot.communication.entity.mai.*;
import com.pygeton.nibot.communication.entity.params.SendMsgParams;
import com.pygeton.nibot.communication.event.IMessageEvent;
import com.pygeton.nibot.communication.service.MaimaiHttpService;
import com.pygeton.nibot.graphic.MaimaiImageGenerator;
import com.pygeton.nibot.repository.pojo.MaimaiChartData;
import com.pygeton.nibot.repository.pojo.MaimaiSongData;
import com.pygeton.nibot.repository.service.AdminDataServiceImpl;
import com.pygeton.nibot.repository.service.MaimaiChartDataServiceImpl;
import com.pygeton.nibot.repository.service.MaimaiRatingDataServiceImpl;
import com.pygeton.nibot.repository.service.MaimaiSongDataServiceImpl;
import com.pygeton.nibot.stat.util.MaimaiStatUtil;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.function.Predicate;

@Component
public class Maimai extends Function implements IMessageEvent {

    @Autowired
    AdminDataServiceImpl adminDataServiceImpl;

    @Autowired
    MaimaiSongDataServiceImpl maimaiSongDataService;

    @Autowired
    MaimaiChartDataServiceImpl maimaiChartDataService;

    @Autowired
    MaimaiRatingDataServiceImpl maimaiRatingDataService;

    @Autowired
    MaimaiHttpService maimaiHttpService;

    @Autowired
    MaimaiImageGenerator maimaiImageGenerator;

    @Autowired
    MaimaiStatUtil maimaiStatUtil;

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
            case "clear" -> clearCache(message.getUserId());
            //常规功能
            case "b50" -> generateB50(message.getUserId());
            case "info" -> getSongInfo();
            case "search" -> searchSong();
            case "add" -> addAilaForSong();
            case "line" -> calculateScoreLine();
            case "plate" -> getPlateProgress(message.getUserId());
            case "rec" -> getRecommendSong(message.getUserId());
            case "status" -> getServerStatus(message.getMessageId());
            case "ds" -> getConstantTable();
            case "list" -> getScoreList(message.getUserId());
        }
    }

    private void initDatabase(Long userId){
        if(adminDataServiceImpl.isAdminExist(userId)){
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
                    boolean chartRet = maimaiChartDataService.updateChartData(maimaiHttpService.getMusicData());
                    if(chartRet){
                        sendMsgParams.addTextMessageSegment("谱面数据库更新完成！\n");
                    }
                    else {
                        sendMsgParams.addTextMessageSegment("谱面数据库更新失败...\n");
                    }
                }
                case "song" -> {
                    boolean songRet = maimaiSongDataService.updateSongData(maimaiHttpService.getMusicData());
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
                case "stats" -> {
                    boolean coverRet = maimaiChartDataService.updateStatList(maimaiHttpService.getChartStats());
                    if(coverRet){
                        sendMsgParams.addTextMessageSegment("歌曲统计数据更新完成！\n");
                    }
                    else {
                        sendMsgParams.addTextMessageSegment("歌曲统计数据更新失败...\n");
                    }
                }
            }
        }
        else {
            sendMsgParams.addTextMessageSegment("你没有使用这个命令的权限喵");
        }
        sendMessage();
    }

    private void clearCache(Long userId){
        if(adminDataServiceImpl.isAdminExist(userId)){
            String dirPath = "D:/Documents/leidian9/Pictures/Maimai/";
            switch (rawMessage[2]){
                case "b50" -> dirPath += "Best50";
                case "line" -> dirPath += "ScoreLine";
                case "rec" -> dirPath += "Recommend";
                case "status" -> dirPath += "Status";
                case "ds" -> dirPath += "ConstantTable";
                case "list" -> dirPath += "ScoreList";
            }
            File dir = new File(dirPath);
            if (dir.isDirectory()) {
                File[] files = dir.listFiles();
                if(files.length > 0){
                    for (File file : files) {
                        if (file.isFile()) {
                            boolean result = file.delete();
                            if (result) {
                                System.out.println("成功删除文件：" + file.getName());
                            } else {
                                System.out.println("删除文件失败：" + file.getName());
                            }
                        }
                    }
                }
                sendMsgParams.addTextMessageSegment("清除缓存成功！");
            }
            else {
                sendMsgParams.addTextMessageSegment("清除缓存失败，请检查参数或路径。");
            }
        }
        else {
            sendMsgParams.addTextMessageSegment("你没有使用这个命令的权限喵");
        }
        sendMessage();
    }

    private void generateB50(Long userId){
        try {
            Map<String, List<JSONObject>> map = maimaiHttpService.getB50(userId);
            if(map.containsKey("400")){
                sendMsgParams.addTextMessageSegment("未找到玩家，可能是查分器账号没有绑定qq，详见/help 6>_<");
            }
            else if(map.containsKey("403")){
                sendMsgParams.addTextMessageSegment("该用户禁止他人访问获取数据=_=");
            }
            else {
                MaimaiBest50 best50 = new MaimaiBest50(map);
                maimaiStatUtil.statRating(userId, best50.getRating());
                for(MaimaiChartInfo chartInfo : best50.getB35List()){
                    int id = chartInfo.getSongId();
                    chartInfo.setCoverUrl("D:/Documents/leidian9/Pictures/Maimai/" + maimaiChartDataService.getCoverUrl(id));
                }
                for(MaimaiChartInfo chartInfo : best50.getB15List()){
                    int id = chartInfo.getSongId();
                    chartInfo.setCoverUrl("D:/Documents/leidian9/Pictures/Maimai/" + maimaiChartDataService.getCoverUrl(id));
                }
                String date = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date());
                String fileName = "mai-b50-" + userId + " " + date + ".png";
                String path = "file:///sdcard/Pictures/Maimai/Best50/" + fileName;
                ImageIO.write(maimaiImageGenerator.generateB50Image(best50), "png", new File("D:/Documents/leidian9/Pictures/Maimai/Best50/" + fileName));
                sendMsgParams.addImageMessageSegment(path);
            }
        }
        catch (Exception e){
            e.printStackTrace();
            sendMsgParams.addTextMessageSegment("服务器出现错误>_<");
        }
        finally {
            sendMessage();
        }
    }

    @Deprecated
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
                MaimaiChartInfo chartInfo = new MaimaiChartInfo();
                chartInfo.setCoverUrl("D:/Documents/leidian9/Pictures/Maimai/" + songData.getCoverUrl());
                chartInfo.setTitle(songData.getTitle());
                chartInfo.setLevelIndex(difficulty.getIndex());
                chartInfo.setType(chartData.getType());
                switch (chartInfo.getLevelIndex()){
                    case 0 -> chartInfo.setLevel(chartData.getBasicLevel());
                    case 1 -> chartInfo.setLevel(chartData.getAdvancedLevel());
                    case 2 -> chartInfo.setLevel(chartData.getExpertLevel());
                    case 3 -> chartInfo.setLevel(chartData.getMasterLevel());
                    case 4 -> chartInfo.setLevel(chartData.getRemasterLevel());
                }
                MaimaiNoteInfo noteInfo = new MaimaiNoteInfo(chartData.getDataList(),difficulty.getIndex());
                //图形化+缓存检测
                String fileName = "mai-sl-" + officialId + "-" + difficulty.getIndex() + ".png";
                String androidPath = "file:///sdcard/Pictures/Maimai/ScoreLine/" + fileName;
                String pcPath = "D:/Documents/leidian9/Pictures/Maimai/ScoreLine/" + fileName;
                if(!Files.exists(Path.of(pcPath))){
                    ImageIO.write(maimaiImageGenerator.generateScoreLineImage(chartInfo,noteInfo), "png", new File(pcPath));
                }
                sendMsgParams.addImageMessageSegment(androidPath);
                //计算目标误差
                if(rawMessage.length == 5){
                    StringBuilder builder = new StringBuilder();
                    double target = Double.parseDouble(rawMessage[4]);
                    double[] bound = noteInfo.getBreakGreatEquivalenceBound();
                    builder.append("达到目标").append(target).append("%允许的最大Tap Great数量为").append((int) Math.floor(noteInfo.getFaultTolerance(target))).append("个\n");
                    builder.append("Break 50落等价于").append(String.format("%.2f",noteInfo.getBreak50Equivalence())).append("个Tap Great\n");
                    builder.append("Break Great相当于").append(String.format("%.2f",bound[0])).append("~").append(String.format("%.2f",bound[1])).append("个Tap Great");
                    sendMsgParams.addTextMessageSegment(builder.toString());
                }

            }
            catch (IndexOutOfBoundsException e){
                e.printStackTrace();
                sendMsgParams.addTextMessageSegment("参数有误，请输入/help 6查看帮助文档>_<");
            }
            catch (NullPointerException e){
                e.printStackTrace();
                sendMsgParams.addTextMessageSegment("歌曲id有误，可能是歌曲不存在或国服未实装，无法获取数据进行计算>_<");
            }
            catch (IOException e){
                e.printStackTrace();
                sendMsgParams.addTextMessageSegment("服务器出现错误>_<");
            }
        }
        else {
            sendMsgParams.addTextMessageSegment("参数有误，请输入/help 6查看帮助文档>_<");
        }
        sendMessage();
    }

    //此功能存在严重问题（舞牌歌曲匹配不正确）
    @SuppressWarnings("unchecked")
    private void getPlateProgress(Long userId){
        if(rawMessage.length == 3){
            MaimaiVersion version = new MaimaiVersion(rawMessage[2].charAt(0));
            if(version.getIndex() == -1){
                sendMsgParams.addTextMessageSegment("输入的版本名不存在，请注意输入>_<");
            }
            else if(version.getIndex() == 12){
                sendMsgParams.addTextMessageSegment("这个版本的牌子还在施工中，敬请期待=_=");
            }
            else if(version.getIndex() == 16){
                sendMsgParams.addTextMessageSegment("国服还没有这个牌子，别急=_=");
            }
            else if(rawMessage[2].equals("真将")){
                sendMsgParams.addTextMessageSegment("输入的牌子类型不存在，请注意输入>_<");
            }
            else {
                List<JSONObject> records = maimaiHttpService.getPlayerRecords(userId);
                List<MaimaiChartData> chartDataList = maimaiChartDataService.getChartDataListByVersion(version.getVersion());
                int total = chartDataList.size();
                Map<String, List<?>> result;
                switch (rawMessage[2].charAt(1)){
                    case '极' -> result = analyzePlateRecords(chartDataList,records,"fc", value -> value.equals("fc") || value.equals("fcp") || value.equals("ap") || value.equals("app"));
                    case '将' -> result = analyzePlateRecords(chartDataList,records,"rate", value -> value.equals("sss") || value.equals("sssp"));
                    case '神' -> result = analyzePlateRecords(chartDataList,records,"fc", value -> value.equals("ap") || value.equals("app"));
                    case '舞' -> result = analyzePlateRecords(chartDataList,records,"fs", value -> value.equals("fsd") || value.equals("fsdp"));
                    default -> {
                        sendMsgParams.addTextMessageSegment("输入的牌子类型不存在，请注意输入>_<");
                        sendMessage();
                        return;
                    }
                }
                List<MaimaiChartData> incompleteMasterList = (List<MaimaiChartData>) result.get("master");
                List<MaimaiChartData> incompleteReMasterList = (List<MaimaiChartData>) result.get("remaster");
                List<Integer> countList = (List<Integer>) result.get("count");
                //后续可能考虑图形化
                StringBuilder builder = new StringBuilder();
                builder.append("你的").append(rawMessage[2]).append("进度如下:\n");
                builder.append("Basic:").append(countList.get(0)).append("/").append(total).append("\n");
                builder.append("Advanced:").append(countList.get(1)).append("/").append(total).append("\n");
                builder.append("Expert:").append(countList.get(2)).append("/").append(total).append("\n");
                builder.append("Master:").append(countList.get(3)).append("/").append(total).append("\n");
                if(version.getVersion().equals("ALL")){
                    builder.append("Re:Master:").append(countList.get(4)).append("/").append(countList.get(5)).append("\n");
                    builder.append("=====================\n");
                    if(incompleteReMasterList.size() > 0){
                        incompleteReMasterList.sort((o1, o2) -> o2.getRemasterLevel().compareTo(o1.getRemasterLevel()));
                        builder.append("尚未完成的较高难度的Re:Master难度歌曲还有:\n");
                        for(MaimaiChartData chartData : incompleteReMasterList){
                            if(chartData.getRemasterLevel().compareTo("13") >= 0){
                                builder.append("(").append(chartData.getRemasterLevel()).append(")").append(maimaiSongDataService.getSongData(chartData.getOfficialId()).getTitle()).append("\n");
                            }
                        }
                    }
                    else {
                        boolean isGet = true;
                        for(Integer count : countList){
                            if (count != total) {
                                isGet = false;
                                break;
                            }
                        }
                        if(isGet){
                            builder.append("你已经").append(rawMessage[2]).append("确认啦，加油清谱吧 (☆^O^☆) ");
                        }
                        else {
                            builder.append("你已经成功取得").append(rawMessage[2]).append("啦，太棒啦Ｏ(≧▽≦)Ｏ");
                        }
                    }
                }
                else {
                    builder.append("=====================\n");
                    if(incompleteMasterList.size() > 0){
                        incompleteMasterList.sort((o1, o2) -> o2.getMasterLevel().compareTo(o1.getMasterLevel()));
                        builder.append("尚未完成的Master难度歌曲还有(至多显示难度较高的前20首):\n");
                        int i = 0;
                        for(MaimaiChartData chartData : incompleteMasterList){
                            builder.append("(").append(chartData.getMasterLevel()).append(")").append(maimaiSongDataService.getSongData(chartData.getOfficialId()).getTitle()).append("\n");
                            i++;
                            if(i >= 20) break;
                        }
                    }
                    else {
                        boolean isGet = true;
                        for(int i = 0;i < 4;i++){
                            int count = countList.get(i);
                            if(count != total){
                                isGet = false;
                                break;
                            }
                        }
                        if(isGet){
                            builder.append("你已经成功取得").append(rawMessage[2]).append("啦，太棒啦Ｏ(≧▽≦)Ｏ");
                        }
                        else {
                            builder.append("你已经").append(rawMessage[2]).append("确认啦，加油清谱吧 (☆^O^☆) ");
                        }
                    }
                }
                sendMsgParams.addTextMessageSegment(builder.toString());
            }
        }
        else {
            sendMsgParams.addTextMessageSegment("参数有误，请输入/help 6查看帮助文档>_<");
        }
        sendMessage();
    }

    private Map<String, List<?>> analyzePlateRecords(List<MaimaiChartData> chartDataList, List<JSONObject> records, String key, Predicate<String> isComplete){
        List<MaimaiChartData> incompleteMasterList = new ArrayList<>(chartDataList);
        List<MaimaiChartData> incompleteReMasterList = new ArrayList<>(chartDataList);
        List<Integer> countList = new ArrayList<>();
        int basCount = 0;
        int advCount = 0;
        int expCount = 0;
        int masCount = 0;
        int reMasCount = 0;
        int reMasTotal = 0;
        for(MaimaiChartData chartData : chartDataList){
            for(JSONObject record : records){
                if(chartData.getOfficialId().equals(record.getIntValue("song_id"))){
                    String value = record.getString(key);
                    boolean complete = isComplete.test(value);
                    switch (record.getString("level_label")){
                        case "Basic" -> {
                            if (complete) basCount++;
                        }
                        case "Advanced" -> {
                            if (complete) advCount++;
                        }
                        case "Expert" -> {
                            if (complete) expCount++;
                        }
                        case "Master" -> {
                            if (complete) {
                                masCount++;
                                incompleteMasterList.remove(chartData);
                            }
                        }
                        case "Re:MASTER" -> {
                            if (complete) {
                                reMasCount++;
                                incompleteReMasterList.remove(chartData);
                            }
                            reMasTotal++;
                        }
                    }
                }
            }
        }
        countList.add(basCount);
        countList.add(advCount);
        countList.add(expCount);
        countList.add(masCount);
        countList.add(reMasCount);
        countList.add(reMasTotal);
        Map<String, List<?>> result = new HashMap<>();
        result.put("count",countList);
        result.put("master",incompleteMasterList);
        result.put("remaster",incompleteReMasterList);
        return result;
    }

    private void getRecommendSong(Long userId){
        Map<String, List<JSONObject>> map = maimaiHttpService.getB50(userId);
        if(map.containsKey("400")){
            sendMsgParams.addTextMessageSegment("未找到玩家，可能是查分器账号没有绑定qq，详见/help 6>_<");
        }
        else if(map.containsKey("403")){
            sendMsgParams.addTextMessageSegment("该用户禁止他人访问获取数据=_=");
        }
        else {
            List<MaimaiChartInfo> b35List = new ArrayList<>();
            List<MaimaiChartInfo> b15List = new ArrayList<>();
            for(JSONObject object : map.get("sd")){
                b35List.add(object.toJavaObject(MaimaiChartInfo.class));
            }
            for(JSONObject object : map.get("dx")){
                b15List.add(object.toJavaObject(MaimaiChartInfo.class));
            }
            int rating = map.get("userdata").get(0).getIntValue("rating");
            boolean isAdvanced = rating > 15500;
            List<MaimaiRecChart> b35RecChartList = getRecommendChart(b35List,false,isAdvanced);
            List<MaimaiRecChart> b15RecChartList = getRecommendChart(b15List,true,isAdvanced);
            try {
                String fileName = "mai-rec-" + userId + "-" + rating + ".png";
                String androidPath = "file:///sdcard/Pictures/Maimai/Recommend/" + fileName;
                String pcPath = "D:/Documents/leidian9/Pictures/Maimai/Recommend/" + fileName;
                if(!Files.exists(Path.of(pcPath))){
                    ImageIO.write(maimaiImageGenerator.generateRecommendImage(b35RecChartList,b15RecChartList), "png", new File(pcPath));
                }
                sendMsgParams.addImageMessageSegment(androidPath);
                StringBuilder builder = new StringBuilder();
                builder.append("镍酱认为你可以试一下推推这些歌 (≧▽≦)\n");
                builder.append("可以结合info功能快速查看谱面信息哦~\n");
                if(b35RecChartList.isEmpty()){
                    builder.append("你的Best35部分已经达到理论最高Rating啦！你好厉害呀OvO\n");
                }
                if(b15RecChartList.isEmpty()){
                    builder.append("你的Best15部分已经达到理论最高Rating啦！你好厉害呀OvO\n");
                }
                sendMsgParams.addTextMessageSegment(builder.toString());
            }
            catch (IOException | NullPointerException e){
                e.printStackTrace();
                sendMsgParams.addTextMessageSegment("服务器出现错误>_<");
            }
        }
        sendMessage();
    }

    private List<MaimaiRecChart> getRecommendChart(List<MaimaiChartInfo> originList,boolean isNew,boolean isAdvanced){
        try {
            Optional<Integer> minRaOpt = originList.stream().map(MaimaiChartInfo::getRa).filter(Objects::nonNull).min(Integer::compare);
            int minRa = minRaOpt.orElse(0);
            List<MaimaiRating> validList = maimaiRatingDataService.getRatingList(minRa);
            validList.sort(Comparator.comparing(MaimaiRating::getRating));
            List<MaimaiRating> SSPlusRating = new ArrayList<>();
            List<MaimaiRating> SSSRating = new ArrayList<>();
            List<MaimaiRating> SSSPlusRating = new ArrayList<>();
            for(MaimaiRating rating : validList){
                switch (rating.getGrade()){
                    case "SS+(MIN)" -> SSPlusRating.add(rating);
                    case "SSS(MIN)" -> SSSRating.add(rating);
                    case "SSS+" -> SSSPlusRating.add(rating);
                }
            }
            List<MaimaiRecChart> allRec = new ArrayList<>();
            //SS+推荐曲目计算
            if(!isAdvanced){
                if(!SSPlusRating.isEmpty()){
                    List<MaimaiRecChart> SSPlusRec = maimaiChartDataService.getRecChartList(SSPlusRating.get(0).getLevel(),SSPlusRating.get(0).getRating(),isNew);
                    List<MaimaiRecChart> SSPlusRemove = new ArrayList<>();
                    for(MaimaiRecChart chart : SSPlusRec){
                        chart.setGradeAndProportion("SS+");
                        for (MaimaiChartInfo info : originList) {
                            if (info.getSongId().equals(chart.getOfficialId())) {
                                SSPlusRemove.add(chart);
                            }
                        }
                    }
                    SSPlusRec.removeAll(SSPlusRemove);
                    SSPlusRec.sort(Comparator.comparing(MaimaiRecChart::getProportion).reversed());
                    if(!SSPlusRec.isEmpty()){
                        allRec.add(SSPlusRec.get(0));
                    }
                }
            }
            //SSS推荐曲目计算
            if(!SSSRating.isEmpty()){
                List<MaimaiRecChart> SSSRec = maimaiChartDataService.getRecChartList(SSSRating.get(0).getLevel(),SSSRating.get(0).getRating(),isNew);
                if(SSSRating.size() > 1){
                    SSSRec.addAll(maimaiChartDataService.getRecChartList(SSSRating.get(1).getLevel(),SSSRating.get(1).getRating(),isNew));
                }
                List<MaimaiRecChart> SSSRemove = new ArrayList<>();
                for(MaimaiRecChart chart : SSSRec){
                    chart.setGradeAndProportion("SSS");
                    for (MaimaiChartInfo info : originList) {
                        if (info.getSongId().equals(chart.getOfficialId())) {
                            SSSRemove.add(chart);
                        }
                    }
                }
                SSSRec.sort(Comparator.comparing(MaimaiRecChart::getProportion).reversed());
                SSSRec.removeAll(SSSRemove);
                for (int i = 0;i < SSSRec.size();i++){
                    if(i == 2) break;
                    allRec.add(SSSRec.get(i));
                }
            }
            //SSS+推荐曲目计算
            if(!SSSPlusRating.isEmpty()){
                List<MaimaiRecChart> SSSPlusRec = maimaiChartDataService.getRecChartList(SSSPlusRating.get(0).getLevel(),SSSPlusRating.get(0).getRating(),isNew);
                if(SSSPlusRating.size() > 1){
                    SSSPlusRec.addAll(maimaiChartDataService.getRecChartList(SSSPlusRating.get(1).getLevel(),SSSPlusRating.get(1).getRating(),isNew));
                }
                if(SSSPlusRating.size() > 2){
                    SSSPlusRec.addAll(maimaiChartDataService.getRecChartList(SSSPlusRating.get(2).getLevel(),SSSPlusRating.get(2).getRating(),isNew));
                }
                List<MaimaiRecChart> SSSPlusRemove = new ArrayList<>();
                for(MaimaiRecChart chart : SSSPlusRec){
                    chart.setGradeAndProportion("SSS+");
                    for (MaimaiChartInfo info : originList) {
                        if (info.getSongId().equals(chart.getOfficialId())) {
                            SSSPlusRemove.add(chart);
                        }
                    }
                }
                SSSPlusRec.sort(Comparator.comparing(MaimaiRecChart::getProportion).reversed());
                SSSPlusRec.removeAll(SSSPlusRemove);
                for (int i = 0;i < SSSPlusRec.size();i++){
                    if(i == 3) break;
                    allRec.add(SSSPlusRec.get(i));
                }
            }
            return allRec;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Deprecated
    private void appendRecInfo(List<MaimaiRecChart> recChartList,boolean isAdvanced,StringBuilder builder){
        MaimaiRecChart recChart;
        for (MaimaiRecChart maimaiRecChart : recChartList) {
            recChart = maimaiRecChart;
            if (!(isAdvanced && recChart.getGrade().equals("SS+"))) {
                builder.append(recChart.getOfficialId()).append(".").append(recChart.getTitle()).append("(").append(recChart.getType()).append(") ").
                        append(recChart.getDifficulty()).append("(").append(recChart.getConstant()).append(") ").append(recChart.getGrade()).append("\n");
            }
        }
    }

    private void getServerStatus(Long messageId){
        System.setProperty("webdriver.chrome.driver","D:/IDE-Enviroment/chromedriver-win64/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        WebDriver driver = new ChromeDriver(options);
        driver.get("https://status.naominet.live/status/wahlap");

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return !!document.querySelector('.main .mb-4')"));
        }
        catch (TimeoutException e){
            sendMsgParams.addReplyMessageSegment(Math.toIntExact(messageId));
            sendMsgParams.addTextMessageSegment("请求超时，请再试一次喵>_<");
            sendMessage();
            return;
        }

        JavascriptExecutor js = (JavascriptExecutor) driver;
        int width = Integer.parseInt(js.executeScript("return document.body.scrollWidth").toString());
        int height = Integer.parseInt(js.executeScript("return document.body.scrollHeight").toString());
        driver.manage().window().setSize(new Dimension(width,height));

        String date = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date());
        String fileName = "mai-status "+ date + ".png";
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(screenshot,new File("D:/Documents/leidian9/Pictures/Maimai/Status/" + fileName));
        }
        catch (IOException e){
            e.printStackTrace();
        }
        String path = "file:///sdcard/Pictures/Maimai/Status/" + fileName;
        driver.quit();
        sendMsgParams.addImageMessageSegment(path);
        sendMessage();
    }

    private void getConstantTable(){
        if(rawMessage.length == 3){
            try {
                String fileName = "mai-ds-" + rawMessage[2] + ".png";
                String androidPath = "file:///sdcard/Pictures/Maimai/ConstantTable/" + fileName;
                String pcPath = "D:/Documents/leidian9/Pictures/Maimai/ConstantTable/" + fileName;
                if(!Files.exists(Path.of(pcPath))) {
                    List<MaimaiTableCell> cellList = maimaiChartDataService.getTableCell(rawMessage[2]);
                    if (cellList.size() == 0) {
                        sendMsgParams.addTextMessageSegment("此功能不支持该等级歌曲>_<");
                    }
                    else {
                        ImageIO.write(maimaiImageGenerator.generateConstantTableImage(rawMessage[2], cellList), "png", new File(pcPath));
                    }
                }
                sendMsgParams.addImageMessageSegment(androidPath);
            }
            catch (IOException e){
                e.printStackTrace();
                sendMsgParams.addTextMessageSegment("服务器出现错误>_<");
            }
        }
        else {
            sendMsgParams.addTextMessageSegment("参数有误，请输入/help 6查看帮助文档>_<");
        }
        sendMessage();
    }

    private void getScoreList(Long userId){
        if (rawMessage.length == 3){
            try {
                List<JSONObject> records = maimaiHttpService.getPlayerRecords(userId);
                List<MaimaiTableCell> cellList = maimaiChartDataService.getTableCell(rawMessage[2]);
                if (cellList.size() == 0) {
                    sendMsgParams.addTextMessageSegment("此功能不支持该等级歌曲>_<");
                }
                else {
                    int[] stat = {0,0,0,0,0,0};
                    double total = 0;
                    for (MaimaiTableCell cell : cellList){
                        for(JSONObject record : records){
                            if(record.getIntValue("song_id") == cell.getOfficialId() && record.getString("level_label").equals(cell.getDifficulty())){
                                cell.setGrade(record.getString("rate"));
                                switch (record.getString("rate")){
                                    case "s" -> stat[0]++;
                                    case "sp" -> stat[1]++;
                                    case "ss" -> stat[2]++;
                                    case "ssp" -> stat[3]++;
                                    case "sss" -> stat[4]++;
                                    case "sssp" -> stat[5]++;
                                }
                                total++;
                                break;
                            }
                        }
                    }
                    for (int i = 5;i > 0;i--){
                        stat[i - 1] += stat[i];
                    }
                    maimaiStatUtil.statGradePercent(userId,rawMessage[2],stat,total);
                    String fileName = "mai-list-" + userId + "-" + rawMessage[2] + ".png";
                    String androidPath = "file:///sdcard/Pictures/Maimai/ScoreList/" + fileName;
                    String pcPath = "D:/Documents/leidian9/Pictures/Maimai/ScoreList/" + fileName;
                    ImageIO.write(maimaiImageGenerator.generateScoreListImage(rawMessage[2], cellList,stat), "png", new File(pcPath));
                    sendMsgParams.addImageMessageSegment(androidPath);
                }
            }
            catch (Exception e){
                e.printStackTrace();
                sendMsgParams.addTextMessageSegment("服务器出现错误>_<");
            }
        }
        else {
            sendMsgParams.addTextMessageSegment("参数有误，请输入/help 6查看帮助文档>_<");
        }
        sendMessage();
    }
}
