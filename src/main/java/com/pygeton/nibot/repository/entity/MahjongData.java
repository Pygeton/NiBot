package com.pygeton.nibot.repository.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("`mahjong_data`")
public class MahjongData {

    @TableId(value = "user_id")
    private Long user_id;

    @TableField(value = "url")
    private String url;
}
