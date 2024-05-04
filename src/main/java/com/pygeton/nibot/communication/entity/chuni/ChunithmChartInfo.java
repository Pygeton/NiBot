package com.pygeton.nibot.communication.entity.chuni;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChunithmChartInfo {

    private String coverUrl;
    private String title;
    private Integer mid;
    private Integer cid;
    private String levelLabel;
    private String level;
    private Integer levelIndex;
    private Float ds;
    private Integer score;
    private Float ra;
    private Double raFine;//精确度更高的ra值
    private String fc;//FC\AJ完成情况
}
