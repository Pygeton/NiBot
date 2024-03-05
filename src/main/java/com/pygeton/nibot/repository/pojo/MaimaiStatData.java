package com.pygeton.nibot.repository.pojo;

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
@TableName("maimai_stat_data")
public class MaimaiStatData {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Long qq;
    private Integer rating;

    @TableField(value = "sss_pct_13_plus")
    private Double sssPct13Plus;

    @TableField(value = "sss_pct_14")
    private Double sssPct14;

    @TableField(value = "sss_pct_14_plus")
    private Double sssPct14Plus;

    @TableField(value = "sssp_pct_13_plus")
    private Double ssspPct13Plus;

    @TableField(value = "sssp_pct_14")
    private Double ssspPct14;

    @TableField(value = "sssp_pct_14_plus")
    private Double ssspPct14Plus;

    public MaimaiStatData(Long qq,Integer rating){
        this.qq = qq;
        this.rating = rating;
    }

    public MaimaiStatData(Long qq,String level,Double sssPct,Double ssspPct){
        this.qq = qq;
        switch (level){
            case "13+" -> {
                sssPct13Plus = sssPct;
                ssspPct13Plus = ssspPct;
            }
            case "14" -> {
                sssPct14 = sssPct;
                ssspPct14 = ssspPct;
            }
            case "14+" -> {
                sssPct14Plus = sssPct;
                ssspPct14Plus = ssspPct;
            }
        }
    }
}
