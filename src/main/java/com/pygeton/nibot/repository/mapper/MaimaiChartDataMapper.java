package com.pygeton.nibot.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pygeton.nibot.repository.entity.MaimaiChartData;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MaimaiChartDataMapper extends BaseMapper<MaimaiChartData> {

    @Select("SELECT title_kana,official_id FROM maimai_chart_data")
    List<MaimaiChartData> getTitleAndOfficialId();

    @Select("SELECT title_kana FROM maimai_song_data WHERE title = #{title}")
    String getTitleKanaFromSongData(String title);

    @Select("SELECT title_kana FROM maimai_chart_data WHERE official_id = #{officialId}")
    String getTitleKanaByOfficialId(int officialId);

    @Select("SELECT official_id FROM maimai_chart_data WHERE title_kana = #{titleKana}")
    List<Integer> getOfficialIdByTitleKana(String titleKana);
}
