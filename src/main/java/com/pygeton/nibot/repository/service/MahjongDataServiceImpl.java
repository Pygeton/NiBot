package com.pygeton.nibot.repository.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pygeton.nibot.repository.entity.MahjongData;
import com.pygeton.nibot.repository.mapper.MahjongDataMapper;
import org.springframework.stereotype.Service;

@Service
public class MahjongDataServiceImpl extends ServiceImpl<MahjongDataMapper, MahjongData> implements IMahjongDataService {

    @Override
    public MahjongData getData(Long id) {
        QueryWrapper<MahjongData> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id);
        return getOne(wrapper);
    }

    @Override
    public boolean saveOrUpdateData(Long id, String name) {
        return saveOrUpdate(new MahjongData(id,name));
    }

    @Override
    public boolean saveOrUpdateData(Long id, String name, Integer area) {
        return saveOrUpdate(new MahjongData(id,name,area));
    }
}
