package com.pygeton.nibot.communication.entity.mai;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaimaiRecChart {

    private Integer officialId;
    private String title;
    private String type;
    private String difficulty;
    private Float constant;
    private String grade;
    private Integer rating;
    private String statList;
    private Double proportion;

    public void setGradeAndProportion(String grade) {
        try {
            JSONObject stat = JSON.parseObject(statList);
            switch (grade) {
                case "SS+" -> {
                    this.grade = grade;
                    switch (difficulty) {
                        case "Expert" -> proportion = stat.getDouble("expSSPlus") + stat.getDouble("expSSS") + stat.getDouble("expSSSPlus");
                        case "Master" -> proportion = stat.getDouble("masSSPlus") + stat.getDouble("masSSS") + stat.getDouble("masSSSPlus");
                        case "Re:Master" -> proportion = stat.getDouble("reMasSSPlus") + stat.getDouble("reMasSSS") + stat.getDouble("reMasSSSPlus");
                    }
                }
                case "SSS" -> {
                    this.grade = grade;
                    switch (difficulty) {
                        case "Expert" -> proportion = stat.getDouble("expSSS") + stat.getDouble("expSSSPlus");
                        case "Master" -> proportion = stat.getDouble("masSSS") + stat.getDouble("masSSSPlus");
                        case "Re:Master" -> proportion = stat.getDouble("reMasSSS") + stat.getDouble("reMasSSSPlus");
                    }
                }
                case "SSS+" -> {
                    this.grade = grade;
                    switch (difficulty) {
                        case "Expert" -> proportion = stat.getDouble("expSSSPlus");
                        case "Master" -> proportion = stat.getDouble("masSSSPlus");
                        case "Re:Master" -> proportion = stat.getDouble("reMasSSSPlus");
                    }
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
            proportion = -1.0;
        }
    }
}
