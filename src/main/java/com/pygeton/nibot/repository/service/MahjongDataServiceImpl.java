package com.pygeton.nibot.repository.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pygeton.nibot.repository.entity.MahjongData;
import com.pygeton.nibot.repository.mapper.MahjongDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MahjongDataServiceImpl extends ServiceImpl<MahjongDataMapper, MahjongData> implements IMahjongDataService {

    @Autowired
    MahjongDataMapper mahjongDataMapper;

    @Override
    public String getUrl(String user_id) {
        LambdaQueryWrapper<MahjongData> wrapper = new LambdaQueryWrapper<>();
        return getOne(wrapper.eq(MahjongData::getUrl,user_id)).getUrl();
    }

    @Override
    public boolean saveOrUpdateUrl(Long user_id, String url) {
        return saveOrUpdate(new MahjongData(user_id,url));
    }
}
