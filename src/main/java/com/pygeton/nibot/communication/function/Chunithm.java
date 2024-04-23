package com.pygeton.nibot.communication.function;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pygeton.nibot.communication.entity.Message;
import com.pygeton.nibot.communication.entity.chuni.ChunithmBest30;
import com.pygeton.nibot.communication.entity.chuni.ChunithmChartInfo;
import com.pygeton.nibot.communication.entity.chuni.ChunithmDifficulty;
import com.pygeton.nibot.communication.entity.chuni.ChunithmRandomChart;
import com.pygeton.nibot.communication.entity.params.SendMsgParams;
import com.pygeton.nibot.communication.event.IMessageEvent;
import com.pygeton.nibot.communication.service.ChunithmHttpService;
import com.pygeton.nibot.graphic.ChunithmImageGenerator;
import com.pygeton.nibot.repository.pojo.ChunithmData;
import com.pygeton.nibot.repository.service.AdminDataServiceImpl;
import com.pygeton.nibot.repository.service.ChunithmDataServiceImpl;
import com.pygeton.nibot.stat.util.ChunithmStatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class Chunithm extends Function implements IMessageEvent {

    @Autowired
    AdminDataServiceImpl adminDataServiceImpl;

    @Autowired
    ChunithmDataServiceImpl chunithmDataService;

    @Autowired
    ChunithmHttpService chunithmHttpService;

    @Autowired
    ChunithmImageGenerator chunithmImageGenerator;

    @Autowired
    ChunithmStatUtil chunithmStatUtil;

    @Override
    public int weight() {
        return 95;
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
            case "clear" -> clearCache(message.getUserId());
            //常规功能
            case "b30" -> generateB30AndR10(message.getUserId());
            case "info" -> getSongInfo();
            case "search" -> searchSong();
            case "add" -> addAilaForSong();
            case "line" -> calculateScoreLine();
            case "random" -> randomSong();
            case "class" -> randomClass();
        }
    }

    private void updateDatabase(Long userId){
        if(adminDataServiceImpl.isAdminExist(userId)){
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

    private void clearCache(Long userId){
        if(adminDataServiceImpl.isAdminExist(userId)){
            String dirPath = "D:/Documents/leidian9/Pictures/Chunithm/";
            if ("b30".equals(rawMessage[2])) {
                dirPath += "Best30";
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

    private void generateB30AndR10(Long userId){
        try {
            Map<String, List<JSONObject>> map = chunithmHttpService.getB30AndR10(userId);
            if(map.containsKey("400")){
                sendMsgParams.addTextMessageSegment("未找到玩家，可能是查分器账号没有绑定qq，详见/help 7>_<");
            }
            else if(map.containsKey("403")){
                sendMsgParams.addTextMessageSegment("该用户禁止他人访问获取数据=_=");
            }
            else {
                ChunithmBest30 best30 = new ChunithmBest30(map);
                chunithmStatUtil.statB30Rating(userId,best30.getB30List());
                for (ChunithmChartInfo chartInfo : best30.getB30List()){
                    int id = chartInfo.getMid();
                    chartInfo.setCoverUrl("D:/Documents/leidian9/Pictures/Chunithm/" + chunithmDataService.getCoverUrl(chartInfo.getMid()));
                }
                for (ChunithmChartInfo chartInfo : best30.getR10List()){
                    int id = chartInfo.getMid();
                    chartInfo.setCoverUrl("D:/Documents/leidian9/Pictures/Chunithm/" + chunithmDataService.getCoverUrl(chartInfo.getMid()));
                }
                String date = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date());
                String fileName = "chu-b30-" + userId + " " + date + ".png";
                String path = "file:///sdcard/Pictures/Chunithm/Best30/" + fileName;
                ImageIO.write(chunithmImageGenerator.generateB30Image(best30), "png", new File("D:/Documents/leidian9/Pictures/Chunithm/Best30/" + fileName));
                sendMsgParams.addImageMessageSegment(path);
            }
        }
        catch (Exception e){
            e.printStackTrace();
            sendMsgParams.addTextMessageSegment("服务器发生错误>_<");
        }
        sendMessage();
    }

    @Deprecated
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
            sendMsgParams.addTextMessageSegment("参数有误，请输入/help 7查看帮助文档>_<");
        }
        sendMessage();
    }

    private void calculateScoreLine(){
        if(rawMessage.length >= 4 && rawMessage.length <= 6){
            int officialId = Integer.parseInt(rawMessage[2]);
            ChunithmDifficulty difficulty;
            try {
                if(rawMessage.length == 4 && officialId / 1000 == 8) {
                    difficulty = new ChunithmDifficulty("WE");
                }
                else {
                    difficulty = new ChunithmDifficulty(rawMessage[3]);
                }
                ChunithmData data = chunithmDataService.getChunithmData(officialId);
                JSONArray dataList = JSON.parseArray(data.getDataList());
                int combo = dataList.getJSONObject(difficulty.getIndex()).getIntValue("combo");
                double justiceCritical = 1010000.0 / combo;//JC分值
                double justice = justiceCritical * 100 / 101;//J分值
                double attack = justiceCritical * 50 / 101;//A分值
                double justiceDeduction = justiceCritical - justice;//J扣分值
                double attackDeduction = justiceCritical - attack;//A扣分值
                //后续可能考虑图形化
                StringBuilder builder = new StringBuilder(rawMessage[2]).append(".").append(data.getTitle());
                builder.append("的").append(difficulty.getDifficulty()).append("难度的误差列表如下：\n");
                builder.append("种类/分值\n");
                builder.append("Justice(小J):").append(String.format("%.4f",-justiceDeduction)).append("\n");
                builder.append("Attack(绿):").append(String.format("%.4f",-attackDeduction)).append("\n");
                builder.append("Miss(灰):").append(String.format("%.4f",-justiceCritical)).append("\n");
                if(rawMessage.length >= 5){
                    double target;
                    switch (rawMessage[4].toLowerCase(Locale.ROOT)){
                        case "ss" -> target = 1000000;
                        case "ss+" -> target = 1005000;
                        case "sss","鸟" -> target = 1007500;
                        case "sss+","鸟加" -> target = 1009000;
                        default -> {
                            target = Double.parseDouble(rawMessage[4]);
                            if(target >= 1000 && target <= 1010){
                                target *= 1000;
                            }
                            else if(target >= 10000 && target <= 10100){
                                target *= 100;
                            }
                        }
                    }
                    double reduce = 1010000 - target;
                    if(reduce < 0 || reduce > 1010000){
                        sendMsgParams.addTextMessageSegment("目标分数不合法，你玩过这游戏吗？=_=");
                    }
                    else {
                        double attackTolerance = Math.floor(reduce / attackDeduction);
                        double missTolerance = Math.floor(reduce / justiceCritical);
                        double justiceTolerance = Math.floor(reduce / justiceDeduction);
                        builder.append("=====================\n");
                        if(attackTolerance > combo){
                            builder.append("目标分数过低，已经不具备计算价值，仅显示误差列表>_<");
                        }
                        else {
                            if(rawMessage.length == 5){
                                double attackReduce = reduce - attackTolerance * attackDeduction;
                                double missReduce = reduce - missTolerance * justiceCritical;
                                builder.append("达到目标").append((int) target).append("允许的误差为：\n");
                                builder.append("最大Attack+Justice(绿+小J)数量为").append((int) attackTolerance).append(" + ").append((int) Math.floor(attackReduce / justiceDeduction)).append("个\n");
                                builder.append("最大Miss+Justice(灰+小J)数量为").append((int) missTolerance).append(" + ").append((int) Math.floor(missReduce / justiceDeduction)).append("个\n");
                                if(justiceTolerance < combo){
                                    builder.append("在AJ条件下，最大Justice(小J)数量为").append((int) justiceTolerance).append("个");
                                }
                            }
                            else {
                                int justiceExpectation = Integer.parseInt(rawMessage[5]);
                                reduce = reduce - justiceDeduction * justiceExpectation;
                                builder.append("在预期Justice(小J)个数为").append(justiceExpectation).append("的条件下，达到目标").append((int) target).append("允许的误差为：\n");
                                builder.append("最大Attack(绿)数量为").append((int) Math.floor(reduce / attackDeduction)).append("个\n");
                                builder.append("最大Miss(灰)数量为").append((int) Math.floor(reduce / justiceCritical)).append("个");
                            }
                        }
                        sendMsgParams.addTextMessageSegment(builder.toString());
                    }
                }
            }
            catch (IndexOutOfBoundsException e){
                e.printStackTrace();
                sendMsgParams.addTextMessageSegment("参数有误，请输入/help 7查看帮助文档>_<");
            }
            catch (NullPointerException e){
                e.printStackTrace();
                sendMsgParams.addTextMessageSegment("歌曲id有误，可能是歌曲不存在或国服未实装，无法获取数据进行计算>_<");
            }
        }
        else {
            sendMsgParams.addTextMessageSegment("参数有误，请输入/help 7查看帮助文档>_<");
        }
        sendMessage();
    }

    private void randomSong(){
        if(rawMessage.length == 3){
            List<ChunithmRandomChart> chartList = chunithmDataService.getRandomChartList(rawMessage[2]);
            Random random = new Random();
            int index = random.nextInt(chartList.size());
            ChunithmRandomChart chart = chartList.get(index);
            sendMsgParams.addTextMessageSegment("镍酱为你随机到了这首歌！\n");
            sendMsgParams.addImageMessageSegment("file:///sdcard/Pictures/Chunithm/" + chart.getCoverUrl());
            sendMsgParams.addTextMessageSegment(chart.getOfficialId() + "." + chart.getTitle() + " " + chart.getDifficulty() + "(" + chart.getConstant() + ")");
        }
        else {
            sendMsgParams.addTextMessageSegment("参数有误，请输入/help 7查看帮助文档>_<");
        }
        sendMessage();
    }

    private void randomClass(){
        if(rawMessage.length == 3){
            Random random = new Random();
            int index;
            List<ChunithmRandomChart> classList = new ArrayList<>();
            switch (rawMessage[2]){
                case "4","IV" -> {
                    List<ChunithmRandomChart> firstChartList = chunithmDataService.getRandomChartList("13+");
                    index = random.nextInt(firstChartList.size());
                    classList.add(firstChartList.get(index));
                    List<ChunithmRandomChart> secondChartList = chunithmDataService.getRandomChartList("14");
                    index = random.nextInt(secondChartList.size());
                    classList.add(secondChartList.get(index));
                    List<ChunithmRandomChart> thirdChartList = chunithmDataService.getRandomChartList("14+");
                    index = random.nextInt(thirdChartList.size());
                    classList.add(thirdChartList.get(index));
                    sendMsgParams.addTextMessageSegment("镍酱为你模拟了段位认定CLASS-IV的随机选曲！\n");
                    showClassChart(classList);
                }
                case "5","V" -> {
                    List<ChunithmRandomChart> chartList = chunithmDataService.getRandomChartList("14+");
                    index = random.nextInt(chartList.size());
                    classList.add(chartList.get(index));
                    index = random.nextInt(chartList.size());
                    classList.add(chartList.get(index));
                    index = random.nextInt(chartList.size());
                    classList.add(chartList.get(index));
                    sendMsgParams.addTextMessageSegment("镍酱为你模拟了段位认定CLASS-V的随机选曲！\n");
                    showClassChart(classList);
                }
                case "无限","∞","♾️" -> {
                    List<ChunithmRandomChart> firstChartList = chunithmDataService.getRandomChartList("14");
                    index = random.nextInt(firstChartList.size());
                    classList.add(firstChartList.get(index));
                    List<ChunithmRandomChart> secondChartList = chunithmDataService.getRandomChartList("14+");
                    index = random.nextInt(secondChartList.size());
                    classList.add(secondChartList.get(index));
                    List<ChunithmRandomChart> thirdChartList = chunithmDataService.getRandomChartList("15");
                    index = random.nextInt(thirdChartList.size());
                    classList.add(thirdChartList.get(index));
                    sendMsgParams.addTextMessageSegment("镍酱为你模拟了段位认定CLASS-∞的随机选曲！\n");
                    showClassChart(classList);
                }
                default -> sendMsgParams.addTextMessageSegment("这是一个不支持的随机段位，请输入/help 7查看帮助文档>_<");
            }
        }
        else {
            sendMsgParams.addTextMessageSegment("参数有误，请输入/help 7查看帮助文档>_<");
        }
        sendMessage();
    }

    private void showClassChart(List<ChunithmRandomChart> classList){
        for (ChunithmRandomChart chart : classList){
            sendMsgParams.addImageMessageSegment("file:///sdcard/Pictures/Chunithm/" + chart.getCoverUrl());
            sendMsgParams.addTextMessageSegment(chart.getOfficialId() + "." + chart.getTitle() + " " + chart.getDifficulty() + "(" + chart.getConstant() + ")");
        }
    }
}
