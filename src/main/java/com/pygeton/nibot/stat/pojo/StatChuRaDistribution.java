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
@TableName("stat_chu_ra_distribution")
public class StatChuRaDistribution {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField(value = "ra_below_1500")
    private Integer raBelow1500;

    @TableField(value = "ra_1500")
    private Integer ra1500;

    @TableField(value = "ra_1525")
    private Integer ra1525;

    @TableField(value = "ra_1550")
    private Integer ra1550;

    @TableField(value = "ra_1575")
    private Integer ra1575;

    @TableField(value = "ra_1600")
    private Integer ra1600;

    @TableField(value = "ra_1625")
    private Integer ra1625;

    @TableField(value = "ra_1650")
    private Integer ra1650;

    @TableField(value = "ra_1675")
    private Integer ra1675;

    @TableField(value = "ra_1700")
    private Integer ra1700;
}
