package com.pygeton.nibot.repository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pygeton.nibot.repository.entity.LongData;

public interface ILongDataService extends IService<LongData> {

    String get(int id);

    boolean add(String url);
}
