package com.pygeton.nibot.communication.entity.mai;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaimaiRecChart {

    private Integer officialId;
    private String title;
    private String type;
    private String difficulty;
    private Float constant;
    private String statList;
}
