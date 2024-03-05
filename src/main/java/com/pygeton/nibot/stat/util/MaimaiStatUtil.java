package com.pygeton.nibot.stat.util;

import com.pygeton.nibot.repository.pojo.MaimaiStatData;
import com.pygeton.nibot.repository.service.MaimaiStatDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MaimaiStatUtil {

    @Autowired
    MaimaiStatDataServiceImpl maimaiStatDataService;

    public void statRating(Long userId,int rating){
        MaimaiStatData statData = maimaiStatDataService.getData(userId);
        if (statData == null){
            statData = new MaimaiStatData(userId,rating);
        }
        else {
            statData.setRating(rating);
        }
        maimaiStatDataService.saveOrUpdateData(statData);
    }

    public void statGradePercent(Long userId,String level,int[] stat,double total){
        MaimaiStatData statData = maimaiStatDataService.getData(userId);
        double sssPct = stat[4] / total;
        double ssspPct = stat[5] / total;
        if (statData == null){
            statData = new MaimaiStatData(userId,level,sssPct,ssspPct);
        }
        else {
            switch (level){
                case "13+" -> {
                    statData.setSssPct13Plus(sssPct);
                    statData.setSsspPct13Plus(ssspPct);
                }
                case "14" -> {
                    statData.setSssPct14(sssPct);
                    statData.setSsspPct14(ssspPct);
                }
                case "14+" -> {
                    statData.setSssPct14Plus(sssPct);
                    statData.setSsspPct14Plus(ssspPct);
                }
            }
        }
        maimaiStatDataService.saveOrUpdateData(statData);
    }
}
