package com.pygeton.nibot.stat.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("stat_mai_grade_avg")
public class StatMaiGradeAvg {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField(value = "sss_pct_avg_13_plus")
    private Double sssPctAvg13Plus;

    @TableField(value = "sssp_pct_avg_13_plus")
    private Double ssspPctAvg13Plus;

    @TableField(value = "sss_pct_avg_14")
    private Double sssPctAvg14;

    @TableField(value = "sssp_pct_avg_14")
    private Double ssspPctAvg14;

    @TableField(value = "sss_pct_avg_14_plus")
    private Double sssPctAvg14Plus;

    @TableField(value = "sssp_pct_avg_14_plus")
    private Double ssspPctAvg14Plus;
}
