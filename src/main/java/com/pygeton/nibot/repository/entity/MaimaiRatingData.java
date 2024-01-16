package com.pygeton.nibot.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("maimai_rating_data")
public class MaimaiRatingData {

    private Float level;
    private Integer sssPlus;
    private Integer sssMax;
    private Integer sssMin;
    private Integer ssPlusMax;
    private Integer ssPlusMin;
}
