package com.pygeton.nibot.repository.entity;

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
    private Integer officialId;
    private String songTitleKana;
    private String songType;
    private String chartBasicRating;
    private Float chartBasicConstant;
    private String chartAdvancedRating;
    private Float chartAdvancedConstant;
    private String chartExpertRating;
    private Float chartExpertConstant;
    private String chartMasterRating;
    private Float chartMasterConstant;
    private String chartRemasterRating;
    private Float chartRemasterConstant;
    private String chartDataList;
    private String chartStatusList;
    private Boolean isNew;
}
