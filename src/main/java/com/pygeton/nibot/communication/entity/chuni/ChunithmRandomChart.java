package com.pygeton.nibot.communication.entity.chuni;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChunithmRandomChart {

    private Integer officialId;
    private String title;
    private String coverUrl;
    private String difficulty;
    private String constant;
}
