package com.pygeton.nibot.repository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pygeton.nibot.repository.entity.MaimaiChartData;

import java.util.List;

public interface IMaimaiChartDataService extends IService<MaimaiChartData> {

    boolean init();

    List<MaimaiChartData> getTitleAndOfficialIdList();
}
