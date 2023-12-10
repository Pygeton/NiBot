package com.pygeton.nibot.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pygeton.nibot.repository.entity.MaimaiSongData;
import org.apache.ibatis.annotations.Select;

public interface MaimaiSongDataMapper extends BaseMapper<MaimaiSongData> {

    @Select("SELECT * FROM maimai_song_data,maimai_chart_data WHERE maimai_song_data.title_kana = maimai_chart_data.title_kana AND official_id = #{officialId}")
    MaimaiSongData getSongDataByOfficialId(int officialId);
}
