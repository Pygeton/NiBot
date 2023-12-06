package com.pygeton.nibot.repository.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pygeton.nibot.repository.entity.MaimaiChartData;

import java.util.List;

public interface IMaimaiChartDataService extends IService<MaimaiChartData> {

    boolean init();

    List<MaimaiChartData> getTitleAndOfficialIdList();

    boolean updateFromJson(List<JSONObject> list);

    //考虑替换成联表查询
    String getTitleKana(int officialId);

    MaimaiChartData getChartData(int officialId);

    List<Integer> getOfficialId(String titleKana);
}
