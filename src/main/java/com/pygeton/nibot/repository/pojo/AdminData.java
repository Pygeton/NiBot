package com.pygeton.nibot.repository.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("admin_data")
public class AdminData {

    Long id;
    Boolean isSuper;
}
