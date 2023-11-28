package com.pygeton.nibot.communication.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pygeton.nibot.communication.entity.mai.Payload;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MaimaiHttpService {

    private final RestTemplate restTemplate;

    public MaimaiHttpService(RestTemplateBuilder builder){
        this.restTemplate = builder.build();
    }

    public Map<String, List<JSONObject>> getB50(Long userId){
        Map<String, List<JSONObject>> map = new HashMap<>();
        Payload payload = new Payload(userId,true);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(JSON.toJSONString(payload),headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange("https://www.diving-fish.com/api/maimaidxprober/query/player", HttpMethod.POST, entity, String.class);
            if(response.getStatusCode() == HttpStatus.OK){
                JSONObject object = JSONObject.parseObject(response.getBody());
                List<JSONObject> sd = object.getJSONObject("charts").getJSONArray("sd").toJavaList(JSONObject.class);
                List<JSONObject> dx = object.getJSONObject("charts").getJSONArray("dx").toJavaList(JSONObject.class);
                for(JSONObject obj : sd){
                    System.out.println(obj);
                }
                for(JSONObject obj : dx){
                    System.out.println(obj);
                }
                map.put("sd",sd);
                map.put("dx",dx);
            }
        }
        catch (HttpClientErrorException e){
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                map.put("400",null);
            }
            else if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
                map.put("403",null);
            }
        }
        return map;
    }
}
