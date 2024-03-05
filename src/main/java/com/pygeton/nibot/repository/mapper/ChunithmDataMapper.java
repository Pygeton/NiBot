package com.pygeton.nibot.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pygeton.nibot.repository.pojo.ChunithmData;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ChunithmDataMapper extends BaseMapper<ChunithmData> {

    @Select("SELECT official_id FROM chunithm_data")
    List<Integer> getOfficialIdList();

    @Select("SELECT cover_url FROM chunithm_data WHERE #{officialId} = official_id")
    String getCoverUrlByOfficialId(int officialId);
}
