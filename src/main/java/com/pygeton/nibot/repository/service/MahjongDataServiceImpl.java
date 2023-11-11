package com.pygeton.nibot.repository.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pygeton.nibot.repository.entity.MahjongData;
import com.pygeton.nibot.repository.mapper.MahjongDataMapper;
import org.springframework.stereotype.Service;

@Service
public class MahjongDataServiceImpl extends ServiceImpl<MahjongDataMapper, MahjongData> implements IMahjongDataService {

    @Override
    public String getUrl(Long user_id) {
        QueryWrapper<MahjongData> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",user_id);
        return getOne(wrapper).getUrl();
    }

    @Override
    public boolean saveOrUpdateUrl(Long user_id, String url) {
        return saveOrUpdate(new MahjongData(user_id,url));
    }
}
