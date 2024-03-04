package com.pygeton.nibot.communication.entity.chuni;

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
public class ChunithmBest30 {

    private String nickname;
    private Double rating;
    private List<ChunithmChartInfo> b30List;
    private List<ChunithmChartInfo> r10List;

    public ChunithmBest30(Map<String, List<JSONObject>> map){
        nickname = map.get("userdata").get(0).getString("nickname");
        rating = map.get("userdata").get(0).getDoubleValue("rating");
        b30List = new ArrayList<>();
        r10List = new ArrayList<>();
        for(JSONObject object : map.get("b30")){
            b30List.add(object.toJavaObject(ChunithmChartInfo.class));
        }
        for(JSONObject object : map.get("r10")){
            r10List.add(object.toJavaObject(ChunithmChartInfo.class));
        }
    }
}
