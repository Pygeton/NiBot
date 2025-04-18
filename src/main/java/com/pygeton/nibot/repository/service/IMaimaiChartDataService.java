package com.pygeton.nibot.repository.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pygeton.nibot.communication.entity.mai.MaimaiRandomChart;
import com.pygeton.nibot.communication.entity.mai.MaimaiRecChart;
import com.pygeton.nibot.communication.entity.mai.MaimaiTableCell;
import com.pygeton.nibot.repository.pojo.MaimaiChartData;

import java.util.List;

public interface IMaimaiChartDataService extends IService<MaimaiChartData> {

    boolean init();

    List<MaimaiChartData> getTitleAndOfficialIdList();

    boolean updateChartData(List<JSONObject> list);

    boolean updateStatList(JSONObject charts);

    MaimaiChartData getChartData(int officialId);

    List<Integer> getOfficialId(String titleKana);

    List<MaimaiChartData> getChartDataListByVersion(String version);

    List<MaimaiRecChart> getRecChartList(float constant,int rating,boolean isNew);

    String getCoverUrl(int officialId);

    List<MaimaiTableCell> getTableCell(String level);

    List<MaimaiRandomChart> getRandomChartList(String level);
}
