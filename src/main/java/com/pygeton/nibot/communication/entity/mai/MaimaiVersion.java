package com.pygeton.nibot.communication.entity.mai;

import lombok.Data;

@Data
public class MaimaiVersion {

    private String version;
    private Integer index;

    public MaimaiVersion(char versionAbbr){
        switch (versionAbbr){
            case '真' -> {
                version = "maimai";
                index = 0;
            }
            case '超' -> {
                version = "maimai GreeN";
                index = 1;
            }
            case '檄' -> {
                version = "maimai GreeN PLUS";
                index = 2;
            }
            case '橙' -> {
                version = "maimai ORANGE";
                index = 3;
            }
            case '晓' -> {
                version = "maimai ORANGE PLUS";
                index = 4;
            }
            case '桃' -> {
                version = "maimai PiNK";
                index = 5;
            }
            case '樱' -> {
                version = "maimai PiNK PLUS";
                index = 6;
            }
            case '紫' -> {
                version = "maimai MURASAKi";
                index = 7;
            }
            case '堇' -> {
                version = "maimai MURASAKi PLUS";
                index = 8;
            }
            case '白' -> {
                version = "maimai MiLK";
                index = 9;
            }
            case '雪' -> {
                version = "maimai MiLK PLUS";
                index = 10;
            }
            case '辉' -> {
                version = "maimai FiNALE";
                index = 11;
            }
            case '舞' -> {
                version = "ALL";
                index = 12;
            }
            case '熊','华' -> {
                version = "maimai でらっくす";
                index = 13;
            }
            case '爽','煌' -> {
                version = "maimai でらっくす Splash";
                index = 14;
            }
            case '宙','星' -> {
                version = "maimai でらっくす UNiVERSE";
                index = 15;
            }
            case '祭','祝' -> {
                version = "maimai でらっくす FESTiVAL";
                index = 16;
            }
            case '双' -> {
                version = "maimai でらっくす BUDDiES";
                index = 17;
            }
            default -> {
                version = "Unknown";
                index = -1;
            }
        }
    }
}
