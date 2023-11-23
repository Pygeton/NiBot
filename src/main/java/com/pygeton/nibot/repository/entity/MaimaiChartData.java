package com.pygeton.nibot.repository.entity;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("maimai_chart_data")
public class MaimaiChartData {

    private Integer id;
    private Integer official_id;
    private String song_title_kana;
    private String song_type;
    private String chart_basic_rating;
    private Float chart_basic_constant;
    private String chart_advanced_rating;
    private Float chart_advanced_constant;
    private String chart_expert_rating;
    private Float chart_expert_constant;
    private String chart_master_rating;
    private Float chart_master_constant;
    private String chart_remaster_rating;
    private Float chart_remaster_constant;
    private JSONArray chart_data_list;
    private JSONArray chart_status_list;
    private Boolean is_new;
}
