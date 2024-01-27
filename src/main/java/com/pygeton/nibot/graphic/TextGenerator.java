package com.pygeton.nibot.graphic;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;

public class TextGenerator {

    public static final String fontPath = "C:/Users/OPE12/AppData/Local/Microsoft/Windows/Fonts/SourceHanSans-";

    public static Font loadFont(String fontType,float fontSize) {
        try {
            File file = new File(fontPath + fontType + ".ttc");
            FileInputStream inputStream = new FileInputStream(file);
            Font dynamicFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            Font dynamicFontPt = dynamicFont.deriveFont(fontSize);
            inputStream.close();
            return dynamicFontPt;
        } catch (Exception e) {
            e.printStackTrace();
            return new java.awt.Font("宋体", Font.PLAIN, (int) fontSize);
        }
    }

    public static String addSpaces(String text) {
        StringBuilder spacedText = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            spacedText.append(text.charAt(i));
            if (i < text.length() - 1) {
                spacedText.append(" ");
            }
        }
        return spacedText.toString();
    }

    public static void drawText(Graphics2D graphics,String text,int maxWidth,int x,int y){
        FontMetrics fontMetrics = graphics.getFontMetrics();
        int textWidth = fontMetrics.stringWidth(text);
        if(textWidth > maxWidth){
            int numChars = text.length() * maxWidth / textWidth;
            String shortTitle = text.substring(0, numChars - 3) + "...";
            graphics.drawString(shortTitle,x,y);
        }
        else {
            graphics.drawString(text,x,y);
        }
    }
}
