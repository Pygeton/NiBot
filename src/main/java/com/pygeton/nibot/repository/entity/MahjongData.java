package com.pygeton.nibot.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("mahjong_data")
public class MahjongData {

    private Long id;
    private String name;
    private Integer area;
}
