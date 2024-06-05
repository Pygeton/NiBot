package com.pygeton.nibot.repository.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("maimai_chart_data")
public class MaimaiChartData {

    //@TableId(type = IdType.AUTO)
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
