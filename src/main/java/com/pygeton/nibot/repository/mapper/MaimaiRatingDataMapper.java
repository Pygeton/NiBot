package com.pygeton.nibot.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pygeton.nibot.communication.entity.mai.MaimaiRating;
import com.pygeton.nibot.repository.entity.MaimaiRatingData;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MaimaiRatingDataMapper extends BaseMapper<MaimaiRatingData> {

    @Select("SELECT level,'SSS+' AS grade,sss_plus AS rating FROM maimai_rating_data WHERE sss_plus > #{value} UNION ALL " +
            "SELECT level,'SSS(MAX)' AS grade,sss_max AS rating FROM maimai_rating_data WHERE sss_max > #{value} UNION ALL " +
            "SELECT level,'SSS(MIN)' AS grade,sss_min AS rating FROM maimai_rating_data WHERE sss_min > #{value} UNION ALL " +
            "SELECT level,'SS+(MAX)' AS grade,ss_plus_max AS rating FROM maimai_rating_data WHERE ss_plus_max > #{value} UNION ALL " +
            "SELECT level,'SS+(MIN)' AS grade,ss_plus_min AS rating FROM maimai_rating_data WHERE ss_plus_min > #{value}")
    List<MaimaiRating> getRatingListGtValue(int value);
}
