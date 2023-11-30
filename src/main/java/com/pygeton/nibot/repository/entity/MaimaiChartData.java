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
    private String titleKana;
    private String type;
    private String version;
    private Boolean isNew;
    private String basicLevel;
    private Float basicConstant;
    private String advancedLevel;
    private Float advancedConstant;
    private String expertLevel;
    private Float expertConstant;
    private String masterLevel;
    private Float masterConstant;
    private String remasterLevel;
    private Float remasterConstant;
    private String dataList;
    private String statList;
}
