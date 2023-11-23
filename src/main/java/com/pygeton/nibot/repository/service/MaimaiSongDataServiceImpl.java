package com.pygeton.nibot.repository.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pygeton.nibot.repository.entity.MaimaiSongData;
import com.pygeton.nibot.repository.mapper.MaimaiSongDataMapper;
import org.springframework.stereotype.Service;

@Service
public class MaimaiSongDataServiceImpl extends ServiceImpl<MaimaiSongDataMapper, MaimaiSongData> implements IMaimaiSongDataService{
}
