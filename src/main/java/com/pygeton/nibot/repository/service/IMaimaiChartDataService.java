package com.pygeton.nibot.repository.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pygeton.nibot.repository.entity.MaimaiChartData;

import java.util.List;

public interface IMaimaiChartDataService extends IService<MaimaiChartData> {

    boolean init();

    List<MaimaiChartData> getTitleAndOfficialIdList();

    boolean updateChartData(List<JSONObject> list);

    boolean updateStatList(JSONObject charts);

    MaimaiChartData getChartData(int officialId);

    List<Integer> getOfficialId(String titleKana);

    List<MaimaiChartData> getChartDataList(String version);
}
