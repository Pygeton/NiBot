package com.pygeton.nibot.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pygeton.nibot.repository.entity.MaimaiChartData;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MaimaiChartDataMapper extends BaseMapper<MaimaiChartData> {

    @Select("SELECT song_title_kana,official_id FROM maimai_chart_data")
    List<MaimaiChartData> selectTitleAndOfficialId();
}
