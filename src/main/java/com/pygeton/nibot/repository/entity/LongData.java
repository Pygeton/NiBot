package com.pygeton.nibot.repository.entity;

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
@TableName("`long_data`")
public class LongData {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    @TableField(value = "url")
    private String url;
}
