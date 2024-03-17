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
@TableName("stat_mai_ra_distribution")
public class StatMaiRaDistribution {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField(value = "ra_below_150")
    private Integer raBelow150;

    @TableField(value = "ra_150")
    private Integer ra150;

    @TableField(value = "ra_151")
    private Integer ra151;

    @TableField(value = "ra_152")
    private Integer ra152;

    @TableField(value = "ra_153")
    private Integer ra153;

    @TableField(value = "ra_154")
    private Integer ra154;

    @TableField(value = "ra_155")
    private Integer ra155;

    @TableField(value = "ra_156")
    private Integer ra156;

    @TableField(value = "ra_157")
    private Integer ra157;

    @TableField(value = "ra_158")
    private Integer ra158;

    @TableField(value = "ra_159")
    private Integer ra159;

    @TableField(value = "ra_160")
    private Integer ra160;

    @TableField(value = "ra_161")
    private Integer ra161;

    @TableField(value = "ra_162")
    private Integer ra162;

    @TableField(value = "ra_163")
    private Integer ra163;

    @TableField(value = "ra_164")
    private Integer ra164;

    @TableField(value = "ra_165")
    private Integer ra165;
}
