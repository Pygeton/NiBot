package com.pygeton.nibot.repository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pygeton.nibot.repository.pojo.MaimaiStatData;

public interface IMaimaiStatDataService extends IService<MaimaiStatData> {

    MaimaiStatData getData(Long qq);

    boolean saveOrUpdateData(MaimaiStatData statData);
}
