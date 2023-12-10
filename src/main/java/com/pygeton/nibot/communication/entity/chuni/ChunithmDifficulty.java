package com.pygeton.nibot.communication.entity.chuni;

import lombok.Data;

@Data
public class ChunithmDifficulty {

    private String difficulty;
    private Integer index;

    public ChunithmDifficulty(String difficulty){
        switch (difficulty){
            case "绿" -> {
                setDifficulty("Basic");
                setIndex(0);
            }
            case "黄" -> {
                setDifficulty("Advanced");
                setIndex(1);
            }
            case "红" -> {
                setDifficulty("Expert");
                setIndex(2);
            }
            case "紫" -> {
                setDifficulty("Master");
                setIndex(3);
            }
            case "黑" -> {
                setDifficulty("Ultima");
                setIndex(4);
            }
            case "WE" -> {
                setDifficulty("World's End");
                setIndex(5);
            }
            default -> {
                setDifficulty("Unknown");
                setIndex(-1);
            }
        }
    }
}
