package com.pygeton.nibot.repository.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pygeton.nibot.repository.pojo.MaimaiStatData;
import com.pygeton.nibot.repository.mapper.MaimaiStatDataMapper;
import org.springframework.stereotype.Service;

@Service
public class MaimaiStatDataServiceImpl extends ServiceImpl<MaimaiStatDataMapper, MaimaiStatData> implements IMaimaiStatDataService{

    @Override
    public MaimaiStatData getData(Long qq) {
        QueryWrapper<MaimaiStatData> wrapper = new QueryWrapper<>();
        wrapper.eq("qq",qq);
        return getOne(wrapper);
    }

    @Override
    public boolean saveOrUpdateData(MaimaiStatData statData) {
        return saveOrUpdate(statData);
    }
}
