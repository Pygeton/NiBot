package com.pygeton.nibot.communication.entity.mai;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class MaimaiNoteInfo {

    private Integer tap;
    private Integer hold;
    private Integer slide;
    private Integer touch;
    private Integer breaks;
    private Double[][] deductions = new Double[7][3];//误差表，下标0-2代表tap-slide，下标3-6代表break

    public MaimaiNoteInfo(String json,int diffIndex){
        JSONArray dataList = JSON.parseArray(json);
        JSONObject data = dataList.getJSONObject(diffIndex);
        JSONArray notes = data.getJSONArray("notes");
        tap = notes.getIntValue(0);
        hold = notes.getIntValue(1);
        slide = notes.getIntValue(2);
        if(notes.size() == 4){
            touch = 0;
            breaks = notes.getIntValue(3);
        }
        else{
            touch = notes.getIntValue(3);
            breaks = notes.getIntValue(4);
        }
        double standardScore = tap * 500 + hold * 1000 + slide * 1500 + breaks * 2500 + touch * 500;
        double extraScore = breaks * 100;
        double standardDeduction = 500 / standardScore * 100;
        //常规note
        for(int i = 0;i < 3;i++){
            //great
            deductions[i][0] = standardDeduction * 0.2 * (i + 1);
            //good
            deductions[i][1] = standardDeduction * 0.5 * (i + 1);
            //miss
            deductions[i][2] = standardDeduction * (i + 1);
        }
        double extraDeduction = 100 / extraScore;
        //绝赞50落和100落
        deductions[3][0] = extraDeduction * 0.25;
        deductions[3][1] = extraDeduction * 0.5;
        //绝赞great
        deductions[4][0] = standardDeduction + extraDeduction * 0.6;
        deductions[4][1] = standardDeduction * 2 + extraDeduction * 0.6;
        deductions[4][2] = standardDeduction * 2.5 + extraDeduction * 0.6;
        //绝赞good
        deductions[5][0] = standardDeduction * 3 + extraDeduction * 0.7;
        //绝赞miss
        deductions[6][0] = standardDeduction * 5 + extraDeduction;
    }

    public int getFullCombo(){
        return tap + hold + slide + touch + breaks;
    }

    public double getFaultTolerance(double target){
        double reduce = 101 - target;
        return reduce / deductions[0][0];
    }

    public double getBreak50Equivalence(){
        return deductions[3][0] / deductions[0][0];
    }

    public double[] getBreakGreatEquivalenceBound(){
        double[] bound = new double[2];
        bound[0] = deductions[4][0] / deductions[0][0];
        bound[1] = deductions[4][2] / deductions[0][0];
        return bound;
    }
}
