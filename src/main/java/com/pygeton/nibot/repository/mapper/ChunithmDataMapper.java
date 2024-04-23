package com.pygeton.nibot.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pygeton.nibot.communication.entity.chuni.ChunithmRandomChart;
import com.pygeton.nibot.repository.pojo.ChunithmData;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ChunithmDataMapper extends BaseMapper<ChunithmData> {

    @Select("SELECT official_id FROM chunithm_data")
    List<Integer> getOfficialIdList();

    @Select("SELECT cover_url FROM chunithm_data WHERE #{officialId} = official_id")
    String getCoverUrlByOfficialId(int officialId);

    @Select("SELECT official_id,title,cover_url,'Expert' AS difficulty,expert_constant AS constant FROM chunithm_data " +
            "WHERE expert_level = #{level} AND is_enabled = 1 AND expert_constant IS NOT NULL UNION ALL " +
            "SELECT official_id,title,cover_url,'Master' AS difficulty,master_constant AS constant FROM chunithm_data " +
            "WHERE master_level = #{level} AND is_enabled = 1 AND master_constant IS NOT NULL UNION ALL " +
            "SELECT official_id,title,cover_url,'Ultima' AS difficulty,ultima_constant AS constant FROM chunithm_data " +
            "WHERE ultima_level = #{level} AND is_enabled = 1 AND ultima_constant IS NOT NULL")
    List<ChunithmRandomChart> getRandomChartByLevel(String level);
}
