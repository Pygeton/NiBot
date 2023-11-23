package com.pygeton.nibot.repository.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pygeton.nibot.repository.entity.MaimaiChartData;
import com.pygeton.nibot.repository.mapper.MaimaiChartDataMapper;
import org.springframework.stereotype.Service;

@Service
public class MaimaiChartDataServiceImpl extends ServiceImpl<MaimaiChartDataMapper, MaimaiChartData> implements IMaimaiChartDataService{
}
