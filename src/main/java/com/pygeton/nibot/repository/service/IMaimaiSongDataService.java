package com.pygeton.nibot.repository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pygeton.nibot.repository.entity.MaimaiChartData;
import com.pygeton.nibot.repository.entity.MaimaiSongData;

import java.util.List;

public interface IMaimaiSongDataService extends IService<MaimaiSongData> {

    boolean init();

    boolean updateCoverUrl(List<MaimaiChartData> list);
}
