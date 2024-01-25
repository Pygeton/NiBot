package com.pygeton.nibot.communication.entity.mai;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaimaiChartInfo {

    private String coverUrl;
    private String title;
    private Integer songId;
    private String type;//SD\DX
    private String levelLabel;
    private String level;
    private Integer levelIndex;
    private Float ds;
    private Double achievements;
    private String rate;
    private Integer dxScore;
    private Integer ra;
    private String fs;//FullSync完成情况
    private String fc;//FC\AP完成情况
}
