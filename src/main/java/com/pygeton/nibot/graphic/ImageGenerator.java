package com.pygeton.nibot.graphic;

import com.pygeton.nibot.communication.entity.mai.MaimaiBest50;
import com.pygeton.nibot.communication.entity.mai.MaimaiChartInfo;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Component
public class ImageGenerator {

    static BufferedImage BOARD,KUMA,DX_LOGO,DIALOG_B35,DIALOG_B15,DECORATE_BG;
    static BufferedImage RATING_1,RATING_2,RATING_3,RATING_4,RATING_5,RATING_6,RATING_7,RATING_8,RATING_9,RATING_10;
    static BufferedImage NUM_0,NUM_1,NUM_2,NUM_3,NUM_4,NUM_5,NUM_6,NUM_7,NUM_8,NUM_9;
    static BufferedImage STD_ICON,DX_ICON;
    static BufferedImage FC_ICON,FCP_ICON,AP_ICON,APP_ICON;
    static BufferedImage FS_ICON,FSP_ICON,FSD_ICON,FSDP_ICON;

    public ImageGenerator() {
        try {
            BOARD = ImageIO.read(getResource("mai/pic/UI_UPE_OsasoiBoard_Base.png"));
            KUMA = ImageIO.read(getResource("mai/template/kuma.jpg"));
            DX_LOGO = ImageIO.read(getResource("mai/pic/UI_CMN_Name_DX.png"));
            DIALOG_B35 = ImageIO.read(getResource("mai/pic/UI_CMN_MiniDialog_b35.png"));
            DIALOG_B15 = ImageIO.read(getResource("mai/pic/UI_CMN_MiniDialog_b15.png"));
            DECORATE_BG = ImageIO.read(getResource("mai/pic/UI_RSL_BG_Parts_01.png"));
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
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private File getResource(String fileName){
        return new File(Objects.requireNonNull(this.getClass().getClassLoader().getResource(fileName)).getPath());
    }

    public BufferedImage generateB50Image(MaimaiBest50 best50) throws IOException {
        List<MaimaiChartInfo> b35List = best50.getB35List();
        List<MaimaiChartInfo> b15List = best50.getB15List();

        BufferedImage template = ImageIO.read(getResource("mai/template/b50.png"));
        Graphics2D graphics = template.createGraphics();

        //绘制头部装饰组件
        graphics.drawImage(BOARD.getScaledInstance(800,250,Image.SCALE_SMOOTH),50,20,null);
        graphics.drawImage(KUMA.getScaledInstance(230,170,Image.SCALE_SMOOTH),90,35,null);
        graphics.drawImage(DX_LOGO.getScaledInstance(100,70,Image.SCALE_SMOOTH),665,40,null);
        graphics.setColor(Color.BLACK);
        graphics.setFont(TextGenerator.loadFont("Regular",72));
        TextGenerator.drawText(graphics,TextGenerator.addSpaces(best50.getNickname()),460,340,170);
        graphics.drawString(TextGenerator.addSpaces(best50.getNickname()),340,170);

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
        for (int i = 0, k = 0; i < 7; i++) {
            x = 50;
            for (int j = 0; j < 5; j++) {
                MaimaiChartInfo chartInfo = b35List.get(k);
                graphics.drawImage(generateMaimaiChartImage(chartInfo,++k),x,y,null);
                addDetail(chartInfo,graphics,x,y);
                x += 340;
            }
            y += 140;
        }

        //绘制中间装饰组件
        graphics.drawImage(DIALOG_B35.getScaledInstance(200,120,Image.SCALE_SMOOTH),50,y,null);
        graphics.drawImage(DIALOG_B15.getScaledInstance(200,120,Image.SCALE_SMOOTH),1530,y,null);
        graphics.drawImage(DECORATE_BG,390,y + 10,null);
        y += 130;

        //绘制B15
        for (int i = 0, k = 0; i < 3; i++) {
            x = 50;
            for (int j = 0; j < 5; j++) {
                MaimaiChartInfo chartInfo = b15List.get(k);
                graphics.drawImage(generateMaimaiChartImage(chartInfo,++k),x,y,null);
                addDetail(chartInfo,graphics,x,y);
                x += 340;
            }
            y += 140;
        }

        graphics.dispose();
        return template;
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

    private BufferedImage generateMaimaiChartImage(MaimaiChartInfo chartInfo, int index) throws IOException {
        //绘制封面
        BufferedImage template = ImageIO.read(getResource("mai/template/chart.png"));
        BufferedImage cover = ImageIO.read(new File(chartInfo.getCoverUrl()));
        Graphics2D graphics = template.createGraphics();
        graphics.drawImage(cover.getScaledInstance(100,100,Image.SCALE_SMOOTH),10,10,null);
        switch (chartInfo.getLevelIndex()){
            case 0 -> graphics.setColor(Color.GREEN);
            case 1 -> graphics.setColor(Color.ORANGE);
            case 2 -> graphics.setColor(Color.decode("#FF6666"));
            case 3 -> graphics.setColor(Color.decode("#9932CC"));
            case 4 -> graphics.setColor(Color.decode("#CC99CC"));
            default -> graphics.setColor(Color.WHITE);
        }
        graphics.fillRect(110,10,200,100);
        if (chartInfo.getLevelIndex() == 4){
            graphics.setColor(Color.BLACK);
        }
        else {
            graphics.setColor(Color.WHITE);
        }
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
}
