package com.pygeton.nibot.communication.entity.mai;

import lombok.Data;

@Data
public class MaimaiDifficulty {

    private String difficulty;
    private Integer index;

    public MaimaiDifficulty(String difficulty){
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
            case "白" -> {
                setDifficulty("Re:Master");
                setIndex(4);
            }
            default -> {
                setDifficulty("Unknown");
                setIndex(-1);
            }
        }
    }
}
