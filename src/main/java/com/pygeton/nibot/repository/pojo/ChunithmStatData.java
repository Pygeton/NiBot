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
@TableName("chunithm_stat_data")
public class ChunithmStatData {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Long qq;
    private Double b30Rating;

    public ChunithmStatData(Long qq,Double b30Rating){
        this.qq = qq;
        this.b30Rating = b30Rating;
    }
}
