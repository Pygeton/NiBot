package com.pygeton.nibot.repository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pygeton.nibot.repository.entity.MahjongData;

public interface IMahjongDataService extends IService<MahjongData> {

    String getUrl(String user_id);

    boolean saveOrUpdateUrl(Long user_id, String url);
}
