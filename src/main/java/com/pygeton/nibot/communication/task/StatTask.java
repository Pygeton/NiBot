package com.pygeton.nibot.communication.task;

import com.pygeton.nibot.repository.pojo.ChunithmStatData;
import com.pygeton.nibot.repository.pojo.MaimaiStatData;
import com.pygeton.nibot.repository.service.ChunithmStatDataServiceImpl;
import com.pygeton.nibot.repository.service.MaimaiStatDataServiceImpl;
import com.pygeton.nibot.stat.pojo.StatChuRaDistribution;
import com.pygeton.nibot.stat.pojo.StatMaiGradeAvg;
import com.pygeton.nibot.stat.pojo.StatMaiRaDistribution;
import com.pygeton.nibot.stat.service.StatChuRaDistributionServiceImpl;
import com.pygeton.nibot.stat.service.StatMaiGradeAvgServiceImpl;
import com.pygeton.nibot.stat.service.StatMaiRaDistributionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StatTask {

    @Autowired
    MaimaiStatDataServiceImpl maimaiStatDataService;

    @Autowired
    ChunithmStatDataServiceImpl chunithmStatDataService;

    @Autowired
    StatMaiRaDistributionServiceImpl maiRaDistributionService;

    @Autowired
    StatMaiGradeAvgServiceImpl maiGradeAvgService;

    @Autowired
    StatChuRaDistributionServiceImpl chuRaDistributionService;

    @Scheduled(fixedRate = 3600000)
    public void execute(){
        try {
            updateMaiStatResult();
            updateChuStatResult();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateMaiStatResult(){
        StatMaiRaDistribution maiRaDistribution = new StatMaiRaDistribution();
        StatMaiGradeAvg maiGradeAvg = new StatMaiGradeAvg();
        List<MaimaiStatData> maimaiStatDataList = maimaiStatDataService.list();

        //统计ra分布
        int raBelow150 = 0,ra150 = 0,ra151 = 0,ra152 = 0,ra153 = 0,ra154 = 0,ra155 = 0,ra156 = 0,ra157 = 0,ra158 = 0,ra159 = 0,ra160 = 0,ra161 = 0,ra162 = 0,ra163 = 0,ra164 = 0,ra165 = 0;
        for (MaimaiStatData statData : maimaiStatDataList){
            if(statData.getRating() != null){
                int rating = statData.getRating();
                if (rating < 15000) raBelow150++;
                else if (rating < 15100) ra150++;
                else if (rating < 15200) ra151++;
                else if (rating < 15300) ra152++;
                else if (rating < 15400) ra153++;
                else if (rating < 15500) ra154++;
                else if (rating < 15600) ra155++;
                else if (rating < 15700) ra156++;
                else if (rating < 15800) ra157++;
                else if (rating < 15900) ra158++;
                else if (rating < 16000) ra159++;
                else if (rating < 16100) ra160++;
                else if (rating < 16200) ra161++;
                else if (rating < 16300) ra162++;
                else if (rating < 16400) ra163++;
                else ra165++;
            }
        }
        maiRaDistribution.setRaBelow150(raBelow150);
        maiRaDistribution.setRa150(ra150);
        maiRaDistribution.setRa151(ra151);
        maiRaDistribution.setRa152(ra152);
        maiRaDistribution.setRa153(ra153);
        maiRaDistribution.setRa154(ra154);
        maiRaDistribution.setRa155(ra155);
        maiRaDistribution.setRa156(ra156);
        maiRaDistribution.setRa157(ra157);
        maiRaDistribution.setRa158(ra158);
        maiRaDistribution.setRa159(ra159);
        maiRaDistribution.setRa160(ra160);
        maiRaDistribution.setRa161(ra161);
        maiRaDistribution.setRa162(ra162);
        maiRaDistribution.setRa163(ra163);
        maiRaDistribution.setRa164(ra164);
        maiRaDistribution.setRa165(ra165);

        maiRaDistributionService.save(maiRaDistribution);

        //统计平均达成率
        double sss13PlusTotal = 0,sssp13PlusTotal = 0,sss14Total = 0,sssp14Total = 0,sss14PlusTotal = 0,sssp14PlusTotal = 0;
        for (MaimaiStatData statData : maimaiStatDataList){
            if(statData.getSssPct13Plus() != null){
                sss13PlusTotal += statData.getSssPct13Plus();
                sssp13PlusTotal += statData.getSsspPct13Plus();
            }
            if(statData.getSssPct14() != null){
                sss14Total += statData.getSssPct14();
                sssp14Total += statData.getSsspPct14();
            }
            if(statData.getSssPct14Plus() != null){
                sss14PlusTotal += statData.getSssPct14Plus();
                sssp14PlusTotal += statData.getSsspPct14Plus();
            }
        }
        int size = maimaiStatDataList.size();
        maiGradeAvg.setSssPctAvg13Plus(sss13PlusTotal / size);
        maiGradeAvg.setSsspPctAvg13Plus(sssp13PlusTotal / size);
        maiGradeAvg.setSssPctAvg14(sss14Total / size);
        maiGradeAvg.setSsspPctAvg14(sssp14Total / size);
        maiGradeAvg.setSssPctAvg14Plus(sss14PlusTotal / size);
        maiGradeAvg.setSsspPctAvg14Plus(sssp14PlusTotal / size);

        maiGradeAvgService.save(maiGradeAvg);
    }

    public void updateChuStatResult(){
        StatChuRaDistribution chuRaDistribution = new StatChuRaDistribution();
        List<ChunithmStatData> chunithmStatDataList = chunithmStatDataService.list();

        int raBelow1500 = 0,ra1500 = 0,ra1525 = 0,ra1550 = 0,ra1575 = 0, ra1600 = 0, ra1625 = 0, ra1650 = 0,ra1675 = 0, ra1700 = 0;
        for (ChunithmStatData statData : chunithmStatDataList){
            double rating = statData.getB30Rating();
            if(rating < 15.00) raBelow1500++;
            else if (rating < 15.25) ra1500++;
            else if (rating < 15.50) ra1525++;
            else if (rating < 15.75) ra1550++;
            else if (rating < 16.00) ra1575++;
            else if (rating < 16.25) ra1600++;
            else if (rating < 16.50) ra1625++;
            else if (rating < 16.75) ra1650++;
            else if (rating < 17.00) ra1675++;
            else ra1700++;
        }
        chuRaDistribution.setRaBelow1500(raBelow1500);
        chuRaDistribution.setRa1500(ra1500);
        chuRaDistribution.setRa1525(ra1525);
        chuRaDistribution.setRa1550(ra1550);
        chuRaDistribution.setRa1575(ra1575);
        chuRaDistribution.setRa1600(ra1600);
        chuRaDistribution.setRa1625(ra1625);
        chuRaDistribution.setRa1650(ra1650);
        chuRaDistribution.setRa1675(ra1675);
        chuRaDistribution.setRa1700(ra1700);

        chuRaDistributionService.save(chuRaDistribution);
    }
}
