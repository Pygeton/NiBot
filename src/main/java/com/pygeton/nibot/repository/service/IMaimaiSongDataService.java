package com.pygeton.nibot.repository.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pygeton.nibot.repository.entity.MaimaiChartData;
import com.pygeton.nibot.repository.entity.MaimaiSongData;

import java.util.List;
import java.util.Map;

public interface IMaimaiSongDataService extends IService<MaimaiSongData> {

    boolean init();

    boolean updateCoverUrl(List<MaimaiChartData> list);

    boolean updateFromJson(List<JSONObject> list);

    MaimaiSongData getSongData(String titleKana);

    Map<String,String> getTitleMap(String keyword);
}
