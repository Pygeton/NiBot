package com.pygeton.nibot.repository.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pygeton.nibot.repository.pojo.ChunithmStatData;
import com.pygeton.nibot.repository.mapper.ChunithmStatDataMapper;
import org.springframework.stereotype.Service;

@Service
public class ChunithmStatDataServiceImpl extends ServiceImpl<ChunithmStatDataMapper, ChunithmStatData> implements IChunithmStatDataService{

    @Override
    public ChunithmStatData getData(Long qq) {
        QueryWrapper<ChunithmStatData> wrapper = new QueryWrapper<>();
        wrapper.eq("qq",qq);
        return getOne(wrapper);
    }

    @Override
    public boolean saveOrUpdateData(ChunithmStatData statData) {
        return saveOrUpdate(statData);
    }
}
