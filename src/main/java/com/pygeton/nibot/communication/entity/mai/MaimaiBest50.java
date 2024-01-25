package com.pygeton.nibot.communication.entity.mai;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaimaiBest50 {

    private String nickname;
    private Integer rating;
    private List<MaimaiChartInfo> b35List;
    private List<MaimaiChartInfo> b15List;

    public MaimaiBest50(Map<String, List<JSONObject>> map){
        nickname = map.get("userdata").get(0).getString("nickname");
        rating = map.get("userdata").get(0).getIntValue("rating");
        b35List = new ArrayList<>();
        b15List = new ArrayList<>();
        for(JSONObject object : map.get("sd")){
            b35List.add(object.toJavaObject(MaimaiChartInfo.class));
        }
        for(JSONObject object : map.get("dx")){
            b15List.add(object.toJavaObject(MaimaiChartInfo.class));
        }
    }
}
