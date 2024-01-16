package com.pygeton.nibot.repository.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pygeton.nibot.communication.entity.mai.MaimaiRating;
import com.pygeton.nibot.repository.entity.MaimaiRatingData;
import com.pygeton.nibot.repository.mapper.MaimaiRatingDataMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaimaiRatingDataServiceImpl extends ServiceImpl<MaimaiRatingDataMapper, MaimaiRatingData> implements IMaimaiRatingDataService {

    @Override
    public List<MaimaiRating> getRatingList(int min) {
        return baseMapper.getRatingListGtValue(min);
    }
}
