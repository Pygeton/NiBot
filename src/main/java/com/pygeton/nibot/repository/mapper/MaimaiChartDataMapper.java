package com.pygeton.nibot.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pygeton.nibot.communication.entity.mai.MaimaiRecChart;
import com.pygeton.nibot.repository.entity.MaimaiChartData;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MaimaiChartDataMapper extends BaseMapper<MaimaiChartData> {

    @Select("SELECT title_kana,official_id FROM maimai_chart_data")
    List<MaimaiChartData> getTitleAndOfficialId();

    @Select("SELECT title_kana FROM maimai_song_data WHERE title = #{title}")
    String getTitleKanaFromSongData(String title);

    @Select("SELECT official_id FROM maimai_chart_data WHERE title_kana = #{titleKana}")
    List<Integer> getOfficialIdByTitleKana(String titleKana);

    @Select("SELECT official_id,title,type,'Expert' AS difficulty,expert_constant AS constant,stat_list,#{rating} AS rating FROM maimai_chart_data,maimai_song_data " +
            "WHERE expert_constant > #{value} - 0.01 AND expert_constant < #{value} + 0.01 AND is_new = #{isNew} AND maimai_chart_data.title_kana = maimai_song_data.title_kana UNION ALL " +
            "SELECT official_id,title,type,'Master' AS difficulty,master_constant AS constant,stat_list,#{rating} AS rating FROM maimai_chart_data,maimai_song_data " +
            "WHERE master_constant > #{value} - 0.01 AND master_constant < #{value} + 0.01 AND is_new = #{isNew} AND maimai_chart_data.title_kana = maimai_song_data.title_kana UNION ALL " +
            "SELECT official_id,title,type,'Re:Master' AS difficulty,remaster_constant AS constant,stat_list,#{rating} AS rating FROM maimai_chart_data,maimai_song_data " +
            "WHERE remaster_constant > #{value} - 0.01 AND remaster_constant < #{value} + 0.01 AND is_new = #{isNew} AND maimai_chart_data.title_kana = maimai_song_data.title_kana")
    List<MaimaiRecChart> getRecChartByConstant(float value,int rating,boolean isNew);

    @Select("SELECT cover_url FROM maimai_chart_data,maimai_song_data WHERE official_id = #{officialId} AND maimai_chart_data.title_kana = maimai_song_data.title_kana")
    String getCoverUrlByOfficialId(int officialId);
}
