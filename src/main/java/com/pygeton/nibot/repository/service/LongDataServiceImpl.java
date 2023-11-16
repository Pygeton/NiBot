package com.pygeton.nibot.repository.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pygeton.nibot.repository.entity.LongData;
import com.pygeton.nibot.repository.mapper.LongDataMapper;
import org.springframework.stereotype.Service;

@Service
public class LongDataServiceImpl extends ServiceImpl<LongDataMapper, LongData> implements ILongDataService{

    @Override
    public String get(int id) {
        LongData data = getById(id);
        return data.getUrl();
    }

    @Override
    public boolean add(String url) {
        LongData data = new LongData();
        data.setUrl(url);
        return save(data);
    }
}
