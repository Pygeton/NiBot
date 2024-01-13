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

    boolean updateSongData(List<JSONObject> list);

    MaimaiSongData getSongData(int officialId);

    //考虑替换为联表查询
    Map<String,String> getTitleMap(String keyword);
}
