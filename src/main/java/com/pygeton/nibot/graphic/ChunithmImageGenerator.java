package com.pygeton.nibot.graphic;

import com.pygeton.nibot.communication.entity.chuni.ChunithmBest30;
import com.pygeton.nibot.communication.entity.chuni.ChunithmChartInfo;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

@Component
public class ChunithmImageGenerator {

    static BufferedImage FC_ICON,AJ_ICON;
    static BufferedImage RAINBOW_1,RAINBOW_2,RAINBOW_3,RAINBOW_4,RAINBOW_5,RAINBOW_6,RAINBOW_7,RAINBOW_8,RAINBOW_9,RAINBOW_0;
    static BufferedImage PLATINUM_1,PLATINUM_2,PLATINUM_3,PLATINUM_4,PLATINUM_5,PLATINUM_6,PLATINUM_7,PLATINUM_8,PLATINUM_9,PLATINUM_0;
    static BufferedImage RAINBOW_POINT,RAINBOW_RATING,PLATINUM_POINT,PLATINUM_RATING;

    public ChunithmImageGenerator(){
        try {
            FC_ICON = ImageIO.read(getResource("chuni/pic/full_combo.png"));
            AJ_ICON = ImageIO.read(getResource("chuni/pic/all_justice.png"));
            PLATINUM_0 = ImageIO.read(getResource("chuni/pic/platinum_0.png"));
            PLATINUM_1 = ImageIO.read(getResource("chuni/pic/platinum_1.png"));
            PLATINUM_2 = ImageIO.read(getResource("chuni/pic/platinum_2.png"));
            PLATINUM_3 = ImageIO.read(getResource("chuni/pic/platinum_3.png"));
            PLATINUM_4 = ImageIO.read(getResource("chuni/pic/platinum_4.png"));
            PLATINUM_5 = ImageIO.read(getResource("chuni/pic/platinum_5.png"));
            PLATINUM_6 = ImageIO.read(getResource("chuni/pic/platinum_6.png"));
            PLATINUM_7 = ImageIO.read(getResource("chuni/pic/platinum_7.png"));
            PLATINUM_8 = ImageIO.read(getResource("chuni/pic/platinum_8.png"));
            PLATINUM_9 = ImageIO.read(getResource("chuni/pic/platinum_9.png"));
            PLATINUM_POINT = ImageIO.read(getResource("chuni/pic/platinum_point.png"));
            PLATINUM_RATING = ImageIO.read(getResource("chuni/pic/platinum_rating.png"));
            RAINBOW_0 = ImageIO.read(getResource("chuni/pic/rainbow_0.png"));
            RAINBOW_1 = ImageIO.read(getResource("chuni/pic/rainbow_1.png"));
            RAINBOW_2 = ImageIO.read(getResource("chuni/pic/rainbow_2.png"));
            RAINBOW_3 = ImageIO.read(getResource("chuni/pic/rainbow_3.png"));
            RAINBOW_4 = ImageIO.read(getResource("chuni/pic/rainbow_4.png"));
            RAINBOW_5 = ImageIO.read(getResource("chuni/pic/rainbow_5.png"));
            RAINBOW_6 = ImageIO.read(getResource("chuni/pic/rainbow_6.png"));
            RAINBOW_7 = ImageIO.read(getResource("chuni/pic/rainbow_7.png"));
            RAINBOW_8 = ImageIO.read(getResource("chuni/pic/rainbow_8.png"));
            RAINBOW_9 = ImageIO.read(getResource("chuni/pic/rainbow_9.png"));
            RAINBOW_POINT = ImageIO.read(getResource("chuni/pic/rainbow_point.png"));
            RAINBOW_RATING = ImageIO.read(getResource("chuni/pic/rainbow_rating.png"));
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private File getResource(String fileName){
        return new File(Objects.requireNonNull(this.getClass().getClassLoader().getResource(fileName)).getPath());
    }

    public BufferedImage generateB30Image(ChunithmBest30 best30){
        try {
            List<ChunithmChartInfo> b30List = best30.getB30List();
            List<ChunithmChartInfo> r10List = best30.getR10List();
            double rating = best30.getRating();
            refreshRating(b30List);
            refreshRating(r10List);
            b30List.sort((o1, o2) -> o2.getRaFine().compareTo(o1.getRaFine()));
            r10List.sort((o1, o2) -> o2.getRaFine().compareTo(o1.getRaFine()));
            for (ChunithmChartInfo chartInfo : b30List){
                System.out.println(chartInfo.getRaFine());
            }

            BufferedImage template = ImageIO.read(getResource("chuni/template/b30.png"));
            Graphics2D graphics = template.createGraphics();

            //绘制用户名和Rating数值
            graphics.setColor(Color.BLACK);
            graphics.setFont(TextGenerator.loadFont("Regular",44));
            TextGenerator.drawCenteredText(graphics,TextGenerator.addSpaces(best30.getNickname()),360,103,85);
            if(rating >= 16){
                graphics.drawImage(RAINBOW_RATING.getScaledInstance(103,25,Image.SCALE_SMOOTH),159,125,null);
            }
            else graphics.drawImage(PLATINUM_RATING.getScaledInstance(103,25,Image.SCALE_SMOOTH),159,125,null);
            BigDecimal bdRating = new BigDecimal(rating);
            bdRating = bdRating.setScale(2, RoundingMode.DOWN);
            String ratingStr = String.format("%.2f",bdRating.doubleValue());
            if(ratingStr.charAt(1) == '.'){
                ratingStr = "0" + ratingStr;
            }
            if(ratingStr.charAt(0) != '0'){
                graphics.drawImage(getNumberImage(rating,ratingStr.charAt(0),23,31,true),289,120,null);
            }
            graphics.drawImage(getNumberImage(rating,ratingStr.charAt(1),23,31,true),313,120,null);
            graphics.drawImage(getNumberImage(rating,ratingStr.charAt(2),23,31,true),342,139,null);
            graphics.drawImage(getNumberImage(rating,ratingStr.charAt(3),23,31,true),358,120,null);
            if(ratingStr.charAt(3) == '1'){
                graphics.drawImage(getNumberImage(rating,ratingStr.charAt(4),23,31,true),383,120,null);
            }
            else {
                graphics.drawImage(getNumberImage(rating,ratingStr.charAt(4),23,31,true),387,120,null);
            }
            //绘制B30
            double b30Rating = drawChartInfo(b30List,graphics,30,262);
            /*
            DecimalFormat b30Mantissa = new DecimalFormat("0.0000");
            graphics.setColor(Color.WHITE);
            graphics.setFont(new Font("Bahnschrift",Font.PLAIN,32));
            graphics.drawString(b30Mantissa.format(b30Rating % 1).substring(4),290,233);
            */
            String b30RatingStr = String.format("%.4f",b30Rating);
            if(b30RatingStr.charAt(1) == '.'){
                b30RatingStr = "0" + b30RatingStr;
            }
            if(b30RatingStr.charAt(0) != '0'){
                graphics.drawImage(getNumberImage(b30Rating,b30RatingStr.charAt(0),19,24,false),169,208,null);
            }
            graphics.drawImage(getNumberImage(b30Rating,b30RatingStr.charAt(1),19,24,false),188,208,null);
            graphics.drawImage(getNumberImage(b30Rating,b30RatingStr.charAt(2),19,24,false),210,224,null);
            graphics.drawImage(getNumberImage(b30Rating,b30RatingStr.charAt(3),19,24,false),221,208,null);
            if(b30RatingStr.charAt(3) == '1'){
                graphics.drawImage(getNumberImage(b30Rating,b30RatingStr.charAt(4),19,24,false),240,208,null);
            }
            else {
                graphics.drawImage(getNumberImage(b30Rating,b30RatingStr.charAt(4),19,24,false),243,208,null);
            }
            graphics.drawImage(getNumberImage(b30Rating,b30RatingStr.charAt(5),13,18,false),265,214,null);
            if(b30RatingStr.charAt(5) == '1'){
                graphics.drawImage(getNumberImage(b30Rating,b30RatingStr.charAt(6),13,18,false),278,214,null);
            }
            else {
                graphics.drawImage(getNumberImage(b30Rating,b30RatingStr.charAt(6),13,18,false),281,214,null);
            }
            //绘制R10
            double r10Rating = drawChartInfo(r10List,graphics,10,1005);
            /*
            DecimalFormat r10Mantissa = new DecimalFormat("0.0000");
            graphics.setColor(Color.WHITE);
            graphics.setFont(new Font("Bahnschrift",Font.PLAIN,32));
            graphics.drawString(r10Mantissa.format(r10Rating % 1).substring(4),290,975);
             */
            String r10RatingStr = String.format("%.4f",r10Rating);
            if(r10RatingStr.charAt(1) == '.'){
                r10RatingStr = "0" + r10RatingStr;
            }
            if(r10RatingStr.charAt(0) != '0'){
                graphics.drawImage(getNumberImage(r10Rating,r10RatingStr.charAt(0),19,24,false),169,951,null);
            }
            graphics.drawImage(getNumberImage(r10Rating,r10RatingStr.charAt(1),19,24,false),188,951,null);
            graphics.drawImage(getNumberImage(r10Rating,r10RatingStr.charAt(2),19,24,false),210,967,null);
            graphics.drawImage(getNumberImage(r10Rating,r10RatingStr.charAt(3),19,24,false),221,951,null);
            if(r10RatingStr.charAt(3) == '1'){
                graphics.drawImage(getNumberImage(r10Rating,r10RatingStr.charAt(4),19,24,false),240,951,null);
            }
            else {
                graphics.drawImage(getNumberImage(r10Rating,r10RatingStr.charAt(4),19,24,false),243,951,null);
            }
            graphics.drawImage(getNumberImage(r10Rating,r10RatingStr.charAt(5),13,18,false),265,957,null);
            if(r10RatingStr.charAt(5) == '1'){
                graphics.drawImage(getNumberImage(r10Rating,r10RatingStr.charAt(6),13,18,false),278,957,null);
            }
            else {
                graphics.drawImage(getNumberImage(r10Rating,r10RatingStr.charAt(6),13,18,false),281,957,null);
            }
            graphics.dispose();
            return template;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private Image getNumberImage(double rating,char num,int w,int h,boolean isHead){
        int fix1W,fixPointW,fixPointH;//数字1和小数点的图像修正值
        if(isHead){
            fix1W = 6;
            fixPointW = 13;
            fixPointH = 20;
        }
        else {
            fix1W = 5;
            fixPointW = 11;
            fixPointH = 16;
        }
        if(rating < 16){
            switch (num){
                case '0' -> {
                    return PLATINUM_0.getScaledInstance(w,h,Image.SCALE_SMOOTH);
                }
                case '1' -> {
                    return PLATINUM_1.getScaledInstance(w - fix1W,h,Image.SCALE_SMOOTH);
                }
                case '2' -> {
                    return PLATINUM_2.getScaledInstance(w,h,Image.SCALE_SMOOTH);
                }
                case '3' -> {
                    return PLATINUM_3.getScaledInstance(w,h,Image.SCALE_SMOOTH);
                }
                case '4' -> {
                    return PLATINUM_4.getScaledInstance(w,h,Image.SCALE_SMOOTH);
                }
                case '5' -> {
                    return PLATINUM_5.getScaledInstance(w,h,Image.SCALE_SMOOTH);
                }
                case '6' -> {
                    return PLATINUM_6.getScaledInstance(w,h,Image.SCALE_SMOOTH);
                }
                case '7' -> {
                    return PLATINUM_7.getScaledInstance(w,h,Image.SCALE_SMOOTH);
                }
                case '8' -> {
                    return PLATINUM_8.getScaledInstance(w,h,Image.SCALE_SMOOTH);
                }
                case '9' -> {
                    return PLATINUM_9.getScaledInstance(w,h,Image.SCALE_SMOOTH);
                }
                case '.' -> {
                    return PLATINUM_POINT.getScaledInstance(w - fixPointW,h - fixPointH,Image.SCALE_SMOOTH);
                }
                default -> {
                    return null;
                }
            }
        }
        else {
            switch (num){
                case '0' -> {
                    return RAINBOW_0.getScaledInstance(w,h,Image.SCALE_SMOOTH);
                }
                case '1' -> {
                    return RAINBOW_1.getScaledInstance(w - fix1W,h,Image.SCALE_SMOOTH);
                }
                case '2' -> {
                    return RAINBOW_2.getScaledInstance(w,h,Image.SCALE_SMOOTH);
                }
                case '3' -> {
                    return RAINBOW_3.getScaledInstance(w,h,Image.SCALE_SMOOTH);
                }
                case '4' -> {
                    return RAINBOW_4.getScaledInstance(w,h,Image.SCALE_SMOOTH);
                }
                case '5' -> {
                    return RAINBOW_5.getScaledInstance(w,h,Image.SCALE_SMOOTH);
                }
                case '6' -> {
                    return RAINBOW_6.getScaledInstance(w,h,Image.SCALE_SMOOTH);
                }
                case '7' -> {
                    return RAINBOW_7.getScaledInstance(w,h,Image.SCALE_SMOOTH);
                }
                case '8' -> {
                    return RAINBOW_8.getScaledInstance(w,h,Image.SCALE_SMOOTH);
                }
                case '9' -> {
                    return RAINBOW_9.getScaledInstance(w,h,Image.SCALE_SMOOTH);
                }
                case '.' -> {
                    return RAINBOW_POINT.getScaledInstance(w - fixPointW,h - fixPointH,Image.SCALE_SMOOTH);
                }
                default -> {
                    return null;
                }
            }
        }
    }

    private double drawChartInfo(List<ChunithmChartInfo> list,Graphics2D graphics,int max,int y){
        BigDecimal ratingSum = BigDecimal.ZERO;
        try {
            int k = 0;
            int x = 50;
            while (k < max){
                if (k < list.size()){
                    ChunithmChartInfo chartInfo = list.get(k);
                    ratingSum = ratingSum.add(BigDecimal.valueOf(chartInfo.getRaFine()));
                    graphics.drawImage(generateChunithmChartImage(chartInfo,++k).getScaledInstance(224,84,Image.SCALE_SMOOTH),x,y,null);
                    switch (chartInfo.getFc()){
                        case "fullcombo" -> graphics.drawImage(FC_ICON,x + 85,y + 84,null);
                        case "alljustice","alljusticecritical" -> graphics.drawImage(AJ_ICON,x + 85,y + 84,null);
                    }
                }
                else {
                    graphics.drawImage(generateEmptyChartImage(++k).getScaledInstance(224,84,Image.SCALE_SMOOTH),x,y,null);
                }
                if(k % 5 == 0){
                    x = 50;
                    y += 108;
                }
                else {
                    x += 244;
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        System.out.println(ratingSum);
        //System.out.println(ratingSum.divide(BigDecimal.valueOf(max),4,RoundingMode.DOWN).doubleValue());
        return ratingSum.divide(BigDecimal.valueOf(max),4,RoundingMode.HALF_UP).doubleValue();
    }

    private BufferedImage generateChunithmChartImage(ChunithmChartInfo chartInfo,int index) throws IOException{
        try {
            BufferedImage template = ImageIO.read(getResource("chuni/template/chart.png"));
            Graphics2D graphics = template.createGraphics();
            //绘制封面
            BufferedImage cover = ImageIO.read(new File(chartInfo.getCoverUrl()));
            graphics.drawImage(cover.getScaledInstance(100,100,Image.SCALE_SMOOTH),10,10,null);
            switch (chartInfo.getLevelIndex()){
                case 0 -> graphics.setColor(Color.decode("#009966"));
                case 1 -> graphics.setColor(Color.decode("#FF9900"));
                case 2 -> graphics.setColor(Color.decode("#FF6666"));
                case 3 -> graphics.setColor(Color.decode("#9932CC"));
                case 4 -> graphics.setColor(Color.BLACK);
                default -> graphics.setColor(Color.WHITE);
            }
            graphics.fillRect(110,10,200,100);
            graphics.setColor(Color.WHITE);
            //绘制标题
            graphics.setFont(TextGenerator.loadFont("Bold",20));
            TextGenerator.drawText(graphics,chartInfo.getTitle(),195,115,35);
            //绘制分数和其他信息
            graphics.setFont(new Font("Bahnschrift",Font.PLAIN,34));
            graphics.drawString(chartInfo.getScore().toString(),115,70);
            graphics.setFont(new Font("Bahnschrift",Font.PLAIN,24));
            graphics.drawString("#" + index + " | " + chartInfo.getDs().toString() + " -> " + String.format("%.2f",chartInfo.getRaFine()),115,100);
            graphics.dispose();
            return template;
        }
        catch (Exception e){
            e.printStackTrace();
            return generateEmptyChartImage(index);
        }
    }

    private BufferedImage generateEmptyChartImage(int index) throws IOException{
        BufferedImage template = ImageIO.read(getResource("chuni/template/chart.png"));
        Graphics2D graphics = template.createGraphics();
        graphics.setColor(Color.BLACK);
        graphics.setFont(TextGenerator.loadFont("Bold",20));
        TextGenerator.drawText(graphics,"-- NO DATA --",195,115,35);
        graphics.setFont(new Font("Bahnschrift",Font.PLAIN,24));
        graphics.drawString("#" + index + " | --",115,100);
        return template;
    }

    private void refreshRating(List<ChunithmChartInfo> list){
        for (ChunithmChartInfo chartInfo : list){
            chartInfo.setRaFine(calculateRating(chartInfo.getScore(),chartInfo.getDs(),chartInfo.getRa()));
        }
    }

    private Double calculateRating(int score, float ds, double rating) {
        BigDecimal raFine;
        BigDecimal scoreBD = new BigDecimal(Integer.toString(score));
        BigDecimal dsBD = new BigDecimal(Float.toString(ds));
        BigDecimal ratingBD = new BigDecimal(Double.toString(rating));
        BigDecimal base = new BigDecimal("975000.0");
        BigDecimal factor = new BigDecimal("0.1");

        if (score < 975000) {
            raFine = ratingBD;
            return raFine.setScale(2, RoundingMode.DOWN).doubleValue();
        } else if (score < 1000000) {
            ratingBD = dsBD.add((scoreBD.subtract(base)).divide(new BigDecimal("2500")).multiply(factor));
        } else if (score < 1005000) {
            ratingBD = dsBD.add(new BigDecimal("1").add((scoreBD.subtract(new BigDecimal("1000000.0"))).divide(new BigDecimal("1000")).multiply(factor)));
        } else if (score < 1007500) {
            ratingBD = dsBD.add(new BigDecimal("1.5").add((scoreBD.subtract(new BigDecimal("1005000.0"))).divide(new BigDecimal("500")).multiply(factor)));
        } else if (score < 1009000) {
            ratingBD = dsBD.add(new BigDecimal("2").add((scoreBD.subtract(new BigDecimal("1007500.0"))).divide(new BigDecimal("1000")).multiply(factor)));
        } else {
            ratingBD = dsBD.add(new BigDecimal("2.15"));
        }
        raFine = ratingBD;
        return raFine.setScale(2, RoundingMode.DOWN).doubleValue();
    }
}
