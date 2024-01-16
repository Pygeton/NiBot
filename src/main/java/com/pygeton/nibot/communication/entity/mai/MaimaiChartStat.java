package com.pygeton.nibot.communication.entity.mai;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaimaiChartStat {

    private Double expSSPlus;
    private Double expSSS;
    private Double expSSSPlus;
    private Double masSSPlus;
    private Double masSSS;
    private Double masSSSPlus;
    private Double reMasSSPlus;
    private Double reMasSSS;
    private Double reMasSSSPlus;
}
