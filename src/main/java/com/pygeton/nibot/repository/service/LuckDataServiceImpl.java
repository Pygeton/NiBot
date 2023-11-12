package com.pygeton.nibot.repository.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pygeton.nibot.repository.entity.LuckData;
import com.pygeton.nibot.repository.mapper.LuckDataMapper;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;

@Service
public class LuckDataServiceImpl extends ServiceImpl<LuckDataMapper, LuckData> implements ILuckDataService {

    @Override
    public int getLuck(Long id) {
        QueryWrapper<LuckData> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id);
        return getOne(wrapper).getLuck();
    }

    @Override
    public boolean saveOrUpdateLuck(Long id, Integer luck) {
        QueryWrapper<LuckData> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id);
        LuckData data = getOne(wrapper);
        Date curDate = new Date(new java.util.Date().getTime());
        if(data != null){
            Date dbDate = data.getDate();
            LocalDate localCurDate = curDate.toLocalDate();
            LocalDate localDbDate = dbDate.toLocalDate();
            if(localDbDate.isBefore(localCurDate)){
                saveOrUpdate(new LuckData(id,curDate,luck));
                return true;
            }
            else return false;
        }
        else {
            saveOrUpdate(new LuckData(id,curDate,luck));
            return true;
        }
    }
}
