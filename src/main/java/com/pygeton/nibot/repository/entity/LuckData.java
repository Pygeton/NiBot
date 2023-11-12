package com.pygeton.nibot.repository.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("`luck_data`")
public class LuckData {

    @TableId(value = "id")
    private Long id;

    @TableField(value = "date")
    private Date date;

    @TableField(value = "luck")
    private Integer luck;
}
