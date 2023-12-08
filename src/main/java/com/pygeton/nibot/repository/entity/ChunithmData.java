package com.pygeton.nibot.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("chunithm_data")
public class ChunithmData {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer officialId;
    private String titleKana;
    private String title;
    private Boolean isEnabled;
    private String artist;
    private Integer bpm;
    private String version;
    private String genre;
    private String basicLevel;
    private Float basicConstant;
    private String advancedLevel;
    private Float advancedConstant;
    private String expertLevel;
    private Float expertConstant;
    private String masterLevel;
    private Float masterConstant;
    private String ultimaLevel;
    private Float ultimaConstant;
    private String weType;
    private Integer weStar;
    private String dataList;
    private String aliasList;
    private String coverUrl;
}
