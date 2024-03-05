package com.pygeton.nibot.repository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pygeton.nibot.repository.pojo.ChunithmStatData;

public interface IChunithmStatDataService extends IService<ChunithmStatData> {

    ChunithmStatData getData(Long qq);

    boolean saveOrUpdateData(ChunithmStatData statData);
}
