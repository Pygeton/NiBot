package com.pygeton.nibot.repository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pygeton.nibot.repository.entity.MahjongData;

public interface IMahjongDataService extends IService<MahjongData> {

    MahjongData getData(Long id);

    boolean saveOrUpdateData(Long id, String name);

    boolean saveOrUpdateData(Long id, String name, Integer area);
}
