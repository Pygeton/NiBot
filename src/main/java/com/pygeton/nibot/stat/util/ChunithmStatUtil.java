package com.pygeton.nibot.stat.util;

import com.pygeton.nibot.communication.entity.chuni.ChunithmChartInfo;
import com.pygeton.nibot.repository.pojo.ChunithmStatData;
import com.pygeton.nibot.repository.service.ChunithmStatDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChunithmStatUtil {

    @Autowired
    ChunithmStatDataServiceImpl chunithmStatDataService;

    public void statB30Rating(Long userId,List<ChunithmChartInfo> b30List){
        double rating = 0;
        for (ChunithmChartInfo chartInfo : b30List){
            rating += chartInfo.getRa();
        }
        rating /= 30;

        ChunithmStatData statData = chunithmStatDataService.getData(userId);
        if(statData == null){
            statData = new ChunithmStatData(userId,rating);
        }
        else {
            statData.setB30Rating(rating);
        }
        chunithmStatDataService.saveOrUpdateData(statData);
    }
}
