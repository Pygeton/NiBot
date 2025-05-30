package com.pygeton.nibot.repository.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pygeton.nibot.communication.entity.chuni.ChunithmRandomChart;
import com.pygeton.nibot.repository.pojo.ChunithmData;

import java.util.List;
import java.util.Map;

public interface IChunithmDataService extends IService<ChunithmData> {

    boolean updateFromSega(List<JSONObject> list);

    boolean updateFromDivingFish(List<JSONObject> list);

    boolean updateCoverUrl();

    ChunithmData getChunithmData(int officialId);

    Map<Integer, String> getResultMap(String keyword);

    String getCoverUrl(int officialId);

    List<ChunithmRandomChart> getRandomChartList(String level);
}
