package com.pygeton.nibot.repository.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pygeton.nibot.repository.pojo.MahjongData;
import com.pygeton.nibot.repository.mapper.MahjongDataMapper;
import org.springframework.stereotype.Service;

@Service
public class MahjongDataServiceImpl extends ServiceImpl<MahjongDataMapper, MahjongData> implements IMahjongDataService {

    @Override
    public MahjongData getData(Long id) {
        return getById(id);
    }

    @Override
    public boolean saveOrUpdateData(Long id, String name) {
        return saveOrUpdate(new MahjongData(id,name,null));
    }

    @Override
    public boolean saveOrUpdateData(Long id, String name, Integer area) {
        return saveOrUpdate(new MahjongData(id,name,area));
    }
}
