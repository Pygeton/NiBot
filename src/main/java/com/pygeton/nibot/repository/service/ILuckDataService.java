package com.pygeton.nibot.repository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pygeton.nibot.repository.entity.LuckData;

public interface ILuckDataService extends IService<LuckData> {

    int getLuck(Long id);

    boolean saveOrUpdateLuck(Long id, Integer luck);
}
