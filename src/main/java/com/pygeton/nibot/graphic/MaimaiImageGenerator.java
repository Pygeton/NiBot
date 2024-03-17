package com.pygeton.nibot.graphic;

import com.pygeton.nibot.communication.entity.mai.*;
import com.pygeton.nibot.repository.service.MaimaiChartDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Component
public class MaimaiImageGenerator {

    static BufferedImage BOARD,KUMA,DX_LOGO,DIALOG_B35,DIALOG_B15,DECORATE_BG,DX_TOKEN;
    static BufferedImage RATING_1,RATING_2,RATING_3,RATING_4,RATING_5,RATING_6,RATING_7,RATING_8,RATING_9,RATING_10;
    static BufferedImage NUM_0,NUM_1,NUM_2,NUM_3,NUM_4,NUM_5,NUM_6,NUM_7,NUM_8,NUM_9;
    static BufferedImage STD_ICON,DX_ICON;
    static BufferedImage FC_ICON,FCP_ICON,AP_ICON,APP_ICON;
    static BufferedImage FS_ICON,FSP_ICON,FSD_ICON,FSDP_ICON;
    static BufferedImage BASE_BSC,BASE_ADV,BASE_EXP,BASE_MST,BASE_MST_RE;
    static BufferedImage RANK_D,RANK_C,RANK_B,RANK_BB,RANK_BBB,RANK_A,RANK_AA,RANK_AAA,RANK_S,RANK_SP,RANK_SS,RANK_SSP,RANK_SSS,RANK_SSSP;

    @Autowired
    MaimaiChartDataServiceImpl maimaiChartDataService;


    public MaimaiImageGenerator() {
        try {
            BOARD = ImageIO.read(getResource("mai/pic/UI_UPE_OsasoiBoard_Base.png"));
            KUMA = ImageIO.read(getResource("mai/template/kuma.jpg"));
            DX_LOGO = ImageIO.read(getResource("mai/pic/UI_CMN_Name_DX.png"));
            DIALOG_B35 = ImageIO.read(getResource("mai/pic/UI_CMN_MiniDialog_b35.png"));
            DIALOG_B15 = ImageIO.read(getResource("mai/pic/UI_CMN_MiniDialog_b15.png"));
            DECORATE_BG = ImageIO.read(getResource("mai/pic/UI_RSL_BG_Parts_01.png"));
            DX_TOKEN = ImageIO.read(getResource("mai/template/dx_token.png"));
            RATING_1 = ImageIO.read(getResource("mai/pic/UI_CMN_DXRating_S_01.png"));
            RATING_2 = ImageIO.read(getResource("mai/pic/UI_CMN_DXRating_S_02.png"));
            RATING_3 = ImageIO.read(getResource("mai/pic/UI_CMN_DXRating_S_03.png"));
            RATING_4 = ImageIO.read(getResource("mai/pic/UI_CMN_DXRating_S_04.png"));
            RATING_5 = ImageIO.read(getResource("mai/pic/UI_CMN_DXRating_S_05.png"));
            RATING_6 = ImageIO.read(getResource("mai/pic/UI_CMN_DXRating_S_06.png"));
            RATING_7 = ImageIO.read(getResource("mai/pic/UI_CMN_DXRating_S_07.png"));
            RATING_8 = ImageIO.read(getResource("mai/pic/UI_CMN_DXRating_S_08.png"));
            RATING_9 = ImageIO.read(getResource("mai/pic/UI_CMN_DXRating_S_09.png"));
            RATING_10 = ImageIO.read(getResource("mai/pic/UI_CMN_DXRating_S_10.png"));
            NUM_0 = ImageIO.read(getResource("mai/pic/UI_NUM_Drating_0.png"));
            NUM_1 = ImageIO.read(getResource("mai/pic/UI_NUM_Drating_1.png"));
            NUM_2 = ImageIO.read(getResource("mai/pic/UI_NUM_Drating_2.png"));
            NUM_3 = ImageIO.read(getResource("mai/pic/UI_NUM_Drating_3.png"));
            NUM_4 = ImageIO.read(getResource("mai/pic/UI_NUM_Drating_4.png"));
            NUM_5 = ImageIO.read(getResource("mai/pic/UI_NUM_Drating_5.png"));
            NUM_6 = ImageIO.read(getResource("mai/pic/UI_NUM_Drating_6.png"));
            NUM_7 = ImageIO.read(getResource("mai/pic/UI_NUM_Drating_7.png"));
            NUM_8 = ImageIO.read(getResource("mai/pic/UI_NUM_Drating_8.png"));
            NUM_9 = ImageIO.read(getResource("mai/pic/UI_NUM_Drating_9.png"));
            STD_ICON = ImageIO.read(getResource("mai/pic/UI_UPE_Infoicon_StandardMode.png"));
            DX_ICON = ImageIO.read(getResource("mai/pic/UI_UPE_Infoicon_DeluxeMode.png"));
            FC_ICON = ImageIO.read(getResource("mai/pic/UI_MSS_MBase_Icon_FC.png"));
            FCP_ICON = ImageIO.read(getResource("mai/pic/UI_MSS_MBase_Icon_FCp.png"));
            AP_ICON = ImageIO.read(getResource("mai/pic/UI_MSS_MBase_Icon_AP.png"));
            APP_ICON = ImageIO.read(getResource("mai/pic/UI_MSS_MBase_Icon_APp.png"));
            FS_ICON = ImageIO.read(getResource("mai/pic/UI_MSS_MBase_Icon_FS.png"));
            FSP_ICON = ImageIO.read(getResource("mai/pic/UI_MSS_MBase_Icon_FSp.png"));
            FSD_ICON = ImageIO.read(getResource("mai/pic/UI_MSS_MBase_Icon_FSD.png"));
            FSDP_ICON = ImageIO.read(getResource("mai/pic/UI_MSS_MBase_Icon_FSDp.png"));
            BASE_BSC = ImageIO.read(getResource("mai/pic/UI_TST_MBase_BSC.png"));
            BASE_ADV = ImageIO.read(getResource("mai/pic/UI_TST_MBase_ADV.png"));
            BASE_EXP = ImageIO.read(getResource("mai/pic/UI_TST_MBase_EXP.png"));
            BASE_MST = ImageIO.read(getResource("mai/pic/UI_TST_MBase_MST.png"));
            BASE_MST_RE = ImageIO.read(getResource("mai/pic/UI_TST_MBase_MST_Re.png"));
            RANK_D = ImageIO.read(getResource("mai/pic/UI_GAM_Rank_D.png"));
            RANK_C = ImageIO.read(getResource("mai/pic/UI_GAM_Rank_C.png"));
            RANK_B = ImageIO.read(getResource("mai/pic/UI_GAM_Rank_B.png"));
            RANK_BB = ImageIO.read(getResource("mai/pic/UI_GAM_Rank_BB.png"));
            RANK_BBB = ImageIO.read(getResource("mai/pic/UI_GAM_Rank_BBB.png"));
            RANK_A = ImageIO.read(getResource("mai/pic/UI_GAM_Rank_A.png"));
            RANK_AA = ImageIO.read(getResource("mai/pic/UI_GAM_Rank_AA.png"));
            RANK_AAA = ImageIO.read(getResource("mai/pic/UI_GAM_Rank_AAA.png"));
            RANK_S = ImageIO.read(getResource("mai/pic/UI_GAM_Rank_S.png"));
            RANK_SP = ImageIO.read(getResource("mai/pic/UI_GAM_Rank_Sp.png"));
            RANK_SS = ImageIO.read(getResource("mai/pic/UI_GAM_Rank_SS.png"));
            RANK_SSP = ImageIO.read(getResource("mai/pic/UI_GAM_Rank_SSp.png"));
            RANK_SSS = ImageIO.read(getResource("mai/pic/UI_GAM_Rank_SSS.png"));
            RANK_SSSP = ImageIO.read(getResource("mai/pic/UI_GAM_Rank_SSSp.png"));
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private File getResource(String fileName){
        return new File(Objects.requireNonNull(this.getClass().getClassLoader().getResource(fileName)).getPath());
    }

    public BufferedImage generateB50Image(MaimaiBest50 best50) {
        try {
            List<MaimaiChartInfo> b35List = best50.getB35List();
            List<MaimaiChartInfo> b15List = best50.getB15List();

            BufferedImage template = ImageIO.read(getResource("mai/template/b50.png"));
            Graphics2D graphics = template.createGraphics();

            //绘制头部装饰组件
            graphics.drawImage(BOARD.getScaledInstance(800,250,Image.SCALE_SMOOTH),50,20,null);
            graphics.drawImage(KUMA.getScaledInstance(230,170,Image.SCALE_SMOOTH),90,35,null);
            graphics.drawImage(DX_LOGO.getScaledInstance(100,70,Image.SCALE_SMOOTH),665,35,null);
            graphics.setColor(Color.BLACK);
            graphics.setFont(TextGenerator.loadFont("Regular",68));
            TextGenerator.drawText(graphics,TextGenerator.addSpaces(best50.getNickname()),460,340,172);

            //匹配rating颜色框
            int rating = best50.getRating();
            if (rating >= 15000){
                graphics.drawImage(RATING_10.getScaledInstance(314,65,Image.SCALE_SMOOTH),320,35,null);
            }
            else if (rating >= 14000){
                graphics.drawImage(RATING_9.getScaledInstance(314,65,Image.SCALE_SMOOTH),320,35,null);
            }
            else if (rating >= 13000){
                graphics.drawImage(RATING_8.getScaledInstance(314,65,Image.SCALE_SMOOTH),320,35,null);
            }
            else if (rating >= 12000){
                graphics.drawImage(RATING_7.getScaledInstance(314,65,Image.SCALE_SMOOTH),320,35,null);
            }
            else if (rating >= 10000){
                graphics.drawImage(RATING_6.getScaledInstance(314,65,Image.SCALE_SMOOTH),320,35,null);
            }
            else if (rating >= 7000){
                graphics.drawImage(RATING_5.getScaledInstance(314,65,Image.SCALE_SMOOTH),320,35,null);
            }
            else if (rating >= 4000){
                graphics.drawImage(RATING_4.getScaledInstance(314,65,Image.SCALE_SMOOTH),320,35,null);
            }
            else if (rating >= 2000){
                graphics.drawImage(RATING_3.getScaledInstance(314,65,Image.SCALE_SMOOTH),320,35,null);
            }
            else if (rating >= 1000){
                graphics.drawImage(RATING_2.getScaledInstance(314,65,Image.SCALE_SMOOTH),320,35,null);
            }
            else {
                graphics.drawImage(RATING_1.getScaledInstance(314,65,Image.SCALE_SMOOTH),320,35,null);
            }

            //绘制rating分数
            int x = 579,y = 52;
            while (rating > 0){
                int digit = rating % 10;
                switch (digit){
                    case 0 -> graphics.drawImage(NUM_0,x,y,null);
                    case 1 -> graphics.drawImage(NUM_1,x,y,null);
                    case 2 -> graphics.drawImage(NUM_2,x,y,null);
                    case 3 -> graphics.drawImage(NUM_3,x,y,null);
                    case 4 -> graphics.drawImage(NUM_4,x,y,null);
                    case 5 -> graphics.drawImage(NUM_5,x,y,null);
                    case 6 -> graphics.drawImage(NUM_6,x,y,null);
                    case 7 -> graphics.drawImage(NUM_7,x,y,null);
                    case 8 -> graphics.drawImage(NUM_8,x,y,null);
                    case 9 -> graphics.drawImage(NUM_9,x,y,null);
                }
                x -= 27;
                rating /= 10;
            }
            y = 250;

            //绘制B35
            y = drawChartInfo(b35List,graphics,35,y);

            //绘制中间装饰组件
            graphics.drawImage(DIALOG_B35.getScaledInstance(200,120,Image.SCALE_SMOOTH),50,y,null);
            graphics.drawImage(DIALOG_B15.getScaledInstance(200,120,Image.SCALE_SMOOTH),1530,y,null);
            graphics.drawImage(DECORATE_BG,390,y + 10,null);
            y += 130;

            //绘制B15
            drawChartInfo(b15List,graphics,15,y);

            graphics.dispose();
            return template;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private int drawChartInfo(List<MaimaiChartInfo> list,Graphics2D graphics,int max,int y){
        try {
            int k = 0;
            int x = 50;
            while (k < max){
                if(k < list.size()){
                    MaimaiChartInfo chartInfo = list.get(k);
                    graphics.drawImage(generateMaimaiChartImage(chartInfo,++k),x,y,null);
                    addDetail(chartInfo,graphics,x,y);
                }
                else {
                    graphics.drawImage(generateEmptyChartImage(++k),x,y,null);
                }
                if(k % 5 == 0){
                    x = 50;
                    y += 140;
                }
                else {
                    x += 340;
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return y;
    }

    private void addDetail(MaimaiChartInfo chartInfo,Graphics2D graphics,int x,int y){
        if (chartInfo.getType().equals("SD")){
            graphics.drawImage(STD_ICON,x,y + 110,null);
        }
        else {
            graphics.drawImage(DX_ICON,x,y + 110,null);
        }
        switch (chartInfo.getFc()){
            case "fc" -> graphics.drawImage(FC_ICON.getScaledInstance(30,30,Image.SCALE_SMOOTH),x + 240,y + 110,null);
            case "fcp" -> graphics.drawImage(FCP_ICON.getScaledInstance(30,30,Image.SCALE_SMOOTH),x + 240,y + 110,null);
            case "ap" -> graphics.drawImage(AP_ICON.getScaledInstance(30,30,Image.SCALE_SMOOTH),x + 240,y + 110,null);
            case "app" -> graphics.drawImage(APP_ICON.getScaledInstance(30,30,Image.SCALE_SMOOTH),x + 240,y + 110,null);
        }
        switch (chartInfo.getFs()){
            case "fs" -> graphics.drawImage(FS_ICON.getScaledInstance(30,30,Image.SCALE_SMOOTH),x + 280,y + 110,null);
            case "fsp" -> graphics.drawImage(FSP_ICON.getScaledInstance(30,30,Image.SCALE_SMOOTH),x + 280,y + 110,null);
            case "fsd" -> graphics.drawImage(FSD_ICON.getScaledInstance(30,30,Image.SCALE_SMOOTH),x + 280,y + 110,null);
            case "fsdp" -> graphics.drawImage(FSDP_ICON.getScaledInstance(30,30,Image.SCALE_SMOOTH),x + 280,y + 110,null);
        }
    }

    private BufferedImage generateMaimaiChartImage(MaimaiChartInfo chartInfo, int index) throws IOException{
        try {
            BufferedImage template = ImageIO.read(getResource("mai/template/chart.png"));
            Graphics2D graphics = template.createGraphics();
            //绘制封面
            BufferedImage cover = ImageIO.read(new File(chartInfo.getCoverUrl()));
            graphics.drawImage(cover.getScaledInstance(100,100,Image.SCALE_SMOOTH),10,10,null);
            switch (chartInfo.getLevelIndex()){
                case 0 -> graphics.setColor(Color.decode("#009966"));
                case 1 -> graphics.setColor(Color.decode("#FF9900"));
                case 2 -> graphics.setColor(Color.decode("#FF6666"));
                case 3 -> graphics.setColor(Color.decode("#9932CC"));
                case 4 -> graphics.setColor(Color.decode("#CC99FF"));
                default -> graphics.setColor(Color.WHITE);
            }
            graphics.fillRect(110,10,200,100);
            graphics.setColor(Color.WHITE);
            //绘制标题
            graphics.setFont(TextGenerator.loadFont("Bold",20));
            TextGenerator.drawText(graphics,chartInfo.getTitle(),195,115,35);
            //绘制分数和其他信息
            graphics.setFont(new Font("Bahnschrift",Font.PLAIN,34));
            graphics.drawString(String.format("%.4f", chartInfo.getAchievements()) + "%",115,70);
            graphics.setFont(new Font("Bahnschrift",Font.PLAIN,24));
            graphics.drawString("#" + index + " | " + chartInfo.getDs().toString() + " -> " + chartInfo.getRa(),115,100);
            graphics.dispose();
            return template;
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println(index + " " + chartInfo.getSongId());
            return generateEmptyChartImage(index);
        }
    }

    private BufferedImage generateEmptyChartImage(int index) throws IOException{
        BufferedImage template = ImageIO.read(getResource("mai/template/chart.png"));
        Graphics2D graphics = template.createGraphics();
        graphics.setColor(Color.BLACK);
        graphics.setFont(TextGenerator.loadFont("Bold",20));
        TextGenerator.drawText(graphics,"-- NO DATA --",195,115,35);
        graphics.setFont(new Font("Bahnschrift",Font.PLAIN,24));
        graphics.drawString("#" + index + " | --",115,100);
        return template;
    }

    public BufferedImage generateScoreLineImage(MaimaiChartInfo chartInfo,MaimaiNoteInfo noteInfo) throws IOException{
        BufferedImage template = ImageIO.read(getResource("mai/template/scoreline.png"));
        Graphics2D graphics = template.createGraphics();
        if(chartInfo.getType().equals("SD")){
            graphics.drawImage(STD_ICON.getScaledInstance(115,28,Image.SCALE_SMOOTH),105,130,null);
        }
        else {
            graphics.drawImage(DX_ICON.getScaledInstance(115,28,Image.SCALE_SMOOTH),105,130,null);
        }
        switch (chartInfo.getLevelIndex()){
            case 0 -> graphics.drawImage(BASE_BSC.getScaledInstance(270,378,Image.SCALE_SMOOTH),105,155,null);
            case 1 -> graphics.drawImage(BASE_ADV.getScaledInstance(270,378,Image.SCALE_SMOOTH),105,155,null);
            case 2 -> graphics.drawImage(BASE_EXP.getScaledInstance(270,378,Image.SCALE_SMOOTH),105,155,null);
            case 3 -> graphics.drawImage(BASE_MST.getScaledInstance(270,378,Image.SCALE_SMOOTH),105,155,null);
            case 4 -> graphics.drawImage(BASE_MST_RE.getScaledInstance(270,378,Image.SCALE_SMOOTH),105,155,null);
        }
        //绘制封面
        BufferedImage cover = ImageIO.read(new File(chartInfo.getCoverUrl()));
        graphics.drawImage(cover.getScaledInstance(220,220,Image.SCALE_SMOOTH),131,175,null);
        //绘制标题
        graphics.setColor(Color.WHITE);
        graphics.setFont(TextGenerator.loadFont("Bold",28));
        TextGenerator.drawCenteredText(graphics,chartInfo.getTitle(),260,110,490);
        //绘制等级
        if(chartInfo.getLevelIndex() < 4){
            graphics.setColor(Color.WHITE);
        }
        else {
            graphics.setColor(Color.decode("#9900CC"));
        }
        graphics.setFont(new Font("Bahnschrift",Font.PLAIN,48));
        if(chartInfo.getLevel().contains("+")){
            graphics.drawString(chartInfo.getLevel(),295,440);
        }
        else {
            graphics.drawString(chartInfo.getLevel(),303,440);
        }
        //绘制误差表
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Bahnschrift",Font.PLAIN,32));
        for(int i = 0,x,y = 232; i < 3; i++){
            x = 830;
            for (int j = 0; j < 3; j++){
                graphics.drawString(String.format("%.4f",-noteInfo.getDeductions()[i][j]) + "%",x,y);
                x += 180;
            }
            y += 60;
        }
        graphics.drawString(String.format("%.4f",-noteInfo.getDeductions()[3][0]) + "%",650,470);
        graphics.drawString(String.format("%.4f",-noteInfo.getDeductions()[3][1]) + "%",650,530);
        for(int i = 0,x = 830,y = 410; i < 3; i++){
            graphics.drawString(String.format("%.4f",-noteInfo.getDeductions()[4][i]) + "%",x,y);
            y += 60;
        }
        graphics.drawString(String.format("%.4f",-noteInfo.getDeductions()[5][0]) + "%",1010,470);
        graphics.drawString(String.format("%.4f",-noteInfo.getDeductions()[6][0]) + "%",1190,470);
        graphics.dispose();
        return template;
    }

    public BufferedImage generateRecommendImage(List<MaimaiRecChart> b35RecChartList,List<MaimaiRecChart> b15RecChartList) throws IOException {
        BufferedImage template = ImageIO.read(getResource("mai/template/rec.png"));
        Graphics2D graphics = template.createGraphics();
        drawRecChart(b35RecChartList,graphics,47,236);
        drawRecChart(b15RecChartList,graphics,542,236);
        return template;
    }

    private void drawRecChart(List<MaimaiRecChart> recChartList,Graphics2D graphics,int x,int y) throws IOException {
        int opx,opy = y;
        for(int i = 0,k = 0; i < 3; i++){
            opx = x;
            for(int j = 0; j < 2; j++){
                if(k >= recChartList.size()) break;
                else {
                    MaimaiRecChart recChart = recChartList.get(k);
                    graphics.drawImage(generateRecChartImage(recChart).getScaledInstance(213,80,Image.SCALE_SMOOTH),opx,opy,null);
                    if(recChart.getType().equals("SD")){
                        graphics.drawImage(STD_ICON.getScaledInstance(80,20,Image.SCALE_SMOOTH),opx,opy + 73,null);
                    }
                    else {
                        graphics.drawImage(DX_ICON.getScaledInstance(80,20,Image.SCALE_SMOOTH),opx,opy + 73,null);
                    }
                    k++;
                    opx += 242;
                }
            }
            opy += 105;
        }
    }

    private BufferedImage generateRecChartImage(MaimaiRecChart recChart) throws IOException{
        BufferedImage template = ImageIO.read(getResource("mai/template/chart.png"));
        Graphics2D graphics = template.createGraphics();
        //绘制封面
        BufferedImage cover = ImageIO.read(new File("D:/Documents/leidian9/Pictures/Maimai/" + maimaiChartDataService.getCoverUrl(recChart.getOfficialId())));
        graphics.drawImage(cover.getScaledInstance(100,100,Image.SCALE_SMOOTH),10,10,null);
        switch (recChart.getDifficulty()){
            case "Expert" -> graphics.setColor(Color.decode("#FF6666"));
            case "Master" -> graphics.setColor(Color.decode("#9932CC"));
            case "Re:Master" -> graphics.setColor(Color.decode("#CC99FF"));
            default -> graphics.setColor(Color.WHITE);
        }
        graphics.fillRect(110,10,200,100);
        graphics.setColor(Color.WHITE);
        //绘制标题
        graphics.setFont(TextGenerator.loadFont("Bold",20));
        TextGenerator.drawText(graphics,recChart.getTitle(),195,115,35);
        //绘制具体信息
        graphics.setFont(new Font("Bahnschrift", Font.BOLD,34));
        graphics.drawString(recChart.getGrade(),115,70);
        graphics.setFont(new Font("Bahnschrift",Font.PLAIN,24));
        graphics.drawString(recChart.getOfficialId() + " | " + recChart.getConstant() + " -> " + recChart.getRating(),115,100);
        graphics.dispose();
        return template;
    }

    public BufferedImage generateConstantTableImage(String level,List<MaimaiTableCell> cellList){
        try {
            switch (level){
                case "13+" -> {
                    BufferedImage template = ImageIO.read(getResource("mai/template/table_13+.png"));
                    Graphics2D graphics = template.createGraphics();
                    int x,y = 177;
                    for (double constant = 13.9;constant >= 13.7;constant -= 0.1){
                        int k = 0,total = 0,i = 0;
                        x = 115;
                        for (MaimaiTableCell cell : cellList){
                            if(cell.getConstant() > constant - 0.01 && cell.getConstant() < constant + 0.01){
                                total++;
                            }
                        }
                        for (MaimaiTableCell cell : cellList){
                            if(cell.getConstant() > constant - 0.01 && cell.getConstant() < constant + 0.01){
                                graphics.drawImage(generateTableCell(cell).getScaledInstance(60,60,Image.SCALE_SMOOTH),x,y,null);
                                k++;
                                if(k == 14 && i < total){
                                    x = 115;
                                    y += 66;
                                    k = 0;
                                }
                                else x += 66;
                            }
                        }
                        y += 66;
                    }
                    graphics.dispose();
                    return template;
                }
                case "14" -> {
                    BufferedImage template = ImageIO.read(getResource("mai/template/table_14.png"));
                    Graphics2D graphics = template.createGraphics();
                    int x,y = 176;
                    for (double constant = 14.6;constant >= 14.0;constant -= 0.1){
                        int k = 0,total = 0,i = 0;
                        x = 132;
                        for (MaimaiTableCell cell : cellList){
                            if(cell.getConstant() > constant - 0.01 && cell.getConstant() < constant + 0.01){
                                total++;
                            }
                        }
                        for (MaimaiTableCell cell : cellList){
                            if(cell.getConstant() > constant - 0.01 && cell.getConstant() < constant + 0.01){
                                graphics.drawImage(generateTableCell(cell).getScaledInstance(60,60,Image.SCALE_SMOOTH),x,y,null);
                                k++;
                                i++;
                                if(k == 12 && i < total){
                                    x = 132;
                                    y += 74;
                                    k = 0;
                                }
                                else x += 74;
                            }
                        }
                        y += 84;
                    }
                    graphics.dispose();
                    return template;
                }
                case "14+","15" -> {
                    BufferedImage template = ImageIO.read(getResource("mai/template/table_14+.png"));
                    Graphics2D graphics = template.createGraphics();
                    int x,y = 176;
                    for (double constant = 15.0;constant >= 14.7;constant -= 0.1){
                        int k = 0,total = 0,i = 0;
                        x = 132;
                        for (MaimaiTableCell cell : cellList){
                            if(cell.getConstant() > constant - 0.01 && cell.getConstant() < constant + 0.01){
                                total++;
                            }
                        }
                        for (MaimaiTableCell cell : cellList){
                            if(cell.getConstant() > constant - 0.01 && cell.getConstant() < constant + 0.01){
                                graphics.drawImage(generateTableCell(cell).getScaledInstance(60,60,Image.SCALE_SMOOTH),x,y,null);
                                k++;
                                i++;
                                if(k == 12 && i < total){
                                    x = 132;
                                    y += 74;
                                    k = 0;
                                }
                                else x += 74;
                            }
                        }
                        y += 84;
                    }
                    graphics.dispose();
                    return template;
                }
                default -> { return null; }
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private BufferedImage generateTableCell(MaimaiTableCell tableCell) throws IOException{
        BufferedImage cover = ImageIO.read(getResource("mai/cover/" + tableCell.getCoverUrl()));
        Image tmp = cover.getScaledInstance(200,200,Image.SCALE_SMOOTH);
        BufferedImage template = new BufferedImage(200,200,BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = template.createGraphics();
        graphics.drawImage(tmp,0,0,null);
        if(tableCell.getDifficulty().equals("Expert")){
            graphics.setColor(Color.decode("#FF6666"));
            graphics.setStroke(new BasicStroke(40));
            graphics.drawRect(0,0,200,200);
        }
        else if (tableCell.getDifficulty().equals("Re:MASTER")){
            graphics.setColor(Color.decode("#CC99FF"));
            graphics.setStroke(new BasicStroke(40));
            graphics.drawRect(0,0,200,200);
        }
        if(tableCell.getType().equals("DX")){
            graphics.drawImage(DX_TOKEN.getScaledInstance(90,45,Image.SCALE_SMOOTH),110,0,null);
        }
        if(tableCell.getGrade() != null){
            switch (tableCell.getGrade()){
                case "sssp" -> graphics.drawImage(RANK_SSSP.getScaledInstance(141,72,Image.SCALE_SMOOTH),40,64,null);
                case "sss" -> graphics.drawImage(RANK_SSS.getScaledInstance(117,69,Image.SCALE_SMOOTH),42,64,null);
                case "ssp" -> graphics.drawImage(RANK_SSP.getScaledInstance(109,69,Image.SCALE_SMOOTH),45,64,null);
                case "ss" -> graphics.drawImage(RANK_SS.getScaledInstance(88,69,Image.SCALE_SMOOTH),56,64,null);
                case "sp" -> graphics.drawImage(RANK_SP.getScaledInstance(72,69,Image.SCALE_SMOOTH),64,66,null);
                case "s" -> graphics.drawImage(RANK_S.getScaledInstance(50,69,Image.SCALE_SMOOTH),75,66,null);
            }
        }
        graphics.dispose();
        return template;
    }

    public BufferedImage generateScoreListImage(String level, List<MaimaiTableCell> cellList, int[] stat){
        try {
            switch (level){
                case "13+" -> {
                    BufferedImage template = ImageIO.read(getResource("mai/template/list_13+.png"));
                    Graphics2D graphics = template.createGraphics();
                    int x,y = 177;
                    for (double constant = 13.9;constant >= 13.7;constant -= 0.1){
                        int k = 0,total = 0,i = 0;
                        x = 115;
                        for (MaimaiTableCell cell : cellList){
                            if(cell.getConstant() > constant - 0.01 && cell.getConstant() < constant + 0.01){
                                total++;
                            }
                        }
                        for (MaimaiTableCell cell : cellList){
                            if(cell.getConstant() > constant - 0.01 && cell.getConstant() < constant + 0.01){
                                graphics.drawImage(generateTableCell(cell).getScaledInstance(60,60,Image.SCALE_SMOOTH),x,y,null);
                                k++;
                                if(k == 14 && i < total){
                                    x = 115;
                                    y += 66;
                                    k = 0;
                                }
                                else x += 66;
                            }
                        }
                        y += 66;
                    }
                    x = 164;
                    y = 133;
                    graphics.setFont(new Font("Bahnschrift", Font.BOLD,36));
                    graphics.setColor(Color.BLACK);
                    for (int i = 0;i < 6;i++){
                        TextGenerator.drawCenteredText(graphics,String.valueOf(stat[i]),100,x,y);
                        x += 102;
                    }
                    TextGenerator.drawCenteredText(graphics,String.valueOf(cellList.size()),100,x,y);
                    graphics.dispose();
                    return template;
                }
                case "14" -> {
                    BufferedImage template = ImageIO.read(getResource("mai/template/list_14.png"));
                    Graphics2D graphics = template.createGraphics();
                    int x,y = 176;
                    for (double constant = 14.6;constant >= 14.0;constant -= 0.1){
                        int k = 0,total = 0,i = 0;
                        x = 132;
                        for (MaimaiTableCell cell : cellList){
                            if(cell.getConstant() > constant - 0.01 && cell.getConstant() < constant + 0.01){
                                total++;
                            }
                        }
                        for (MaimaiTableCell cell : cellList){
                            if(cell.getConstant() > constant - 0.01 && cell.getConstant() < constant + 0.01){
                                graphics.drawImage(generateTableCell(cell).getScaledInstance(60,60,Image.SCALE_SMOOTH),x,y,null);
                                k++;
                                i++;
                                if(k == 12 && i < total){
                                    x = 132;
                                    y += 74;
                                    k = 0;
                                }
                                else x += 74;
                            }
                        }
                        y += 84;
                    }
                    x = 164;
                    y = 133;
                    graphics.setFont(new Font("Bahnschrift", Font.BOLD,36));
                    graphics.setColor(Color.BLACK);
                    for (int i = 0;i < 6;i++){
                        TextGenerator.drawCenteredText(graphics,String.valueOf(stat[i]),100,x,y);
                        x += 102;
                    }
                    TextGenerator.drawCenteredText(graphics,String.valueOf(cellList.size()),100,x,y);
                    graphics.dispose();
                    return template;
                }
                case "14+","15" -> {
                    BufferedImage template = ImageIO.read(getResource("mai/template/list_14+.png"));
                    Graphics2D graphics = template.createGraphics();
                    int x,y = 176;
                    for (double constant = 15.0;constant >= 14.7;constant -= 0.1){
                        int k = 0,total = 0,i = 0;
                        x = 132;
                        for (MaimaiTableCell cell : cellList){
                            if(cell.getConstant() > constant - 0.01 && cell.getConstant() < constant + 0.01){
                                total++;
                            }
                        }
                        for (MaimaiTableCell cell : cellList){
                            if(cell.getConstant() > constant - 0.01 && cell.getConstant() < constant + 0.01){
                                graphics.drawImage(generateTableCell(cell).getScaledInstance(60,60,Image.SCALE_SMOOTH),x,y,null);
                                k++;
                                i++;
                                if(k == 12 && i < total){
                                    x = 132;
                                    y += 74;
                                    k = 0;
                                }
                                else x += 74;
                            }
                        }
                        y += 84;
                    }
                    x = 164;
                    y = 133;
                    graphics.setFont(new Font("Bahnschrift", Font.BOLD,36));
                    graphics.setColor(Color.BLACK);
                    for (int i = 0;i < 6;i++){
                        TextGenerator.drawCenteredText(graphics,String.valueOf(stat[i]),100,x,y);
                        x += 102;
                    }
                    TextGenerator.drawCenteredText(graphics,String.valueOf(cellList.size()),100,x,y);
                    graphics.dispose();
                    return template;
                }
                default -> { return null; }
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
