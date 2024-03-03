package com.pygeton.nibot.communication.entity.mai;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaimaiTableCell {

    private Integer officialId;
    private String title;
    private String coverUrl;
    private String type;
    private String difficulty;
    private Float constant;
    private String grade;
}
