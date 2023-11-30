package com.pygeton.nibot.communication.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pygeton.nibot.communication.entity.mai.MaimaiPayload;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class MaimaiHttpService {

    private final RestTemplate restTemplate;

    public MaimaiHttpService(RestTemplateBuilder builder){
        this.restTemplate = builder.build();
    }

    public Map<String, List<JSONObject>> getB50(Long userId){
        Map<String, List<JSONObject>> map = new HashMap<>();
        MaimaiPayload payload = new MaimaiPayload(userId,true);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(JSON.toJSONString(payload),headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange("https://www.diving-fish.com/api/maimaidxprober/query/player", HttpMethod.POST, entity, String.class);
            if(response.getStatusCode() == HttpStatus.OK){
                JSONObject object = JSONObject.parseObject(response.getBody());
                List<JSONObject> userdata = new ArrayList<>();
                JSONObject userObject = new JSONObject();
                userObject.put("nickname",object.getString("nickname"));
                userObject.put("rating",object.getIntValue("rating"));
                userdata.add(userObject);
                List<JSONObject> sd = object.getJSONObject("charts").getJSONArray("sd").toJavaList(JSONObject.class);
                List<JSONObject> dx = object.getJSONObject("charts").getJSONArray("dx").toJavaList(JSONObject.class);
                map.put("userdata",userdata);
                map.put("sd",sd);
                map.put("dx",dx);
            }
        }
        catch (HttpClientErrorException e){
            if(e.getStatusCode() == HttpStatus.BAD_REQUEST){
                map.put("400",null);
            }
            else if(e.getStatusCode() == HttpStatus.FORBIDDEN){
                map.put("403",null);
            }
        }
        return map;
    }

    public List<JSONObject> getMusicData(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange("https://www.diving-fish.com/api/maimaidxprober/music_data", HttpMethod.GET, entity, String.class);
            if(response.getStatusCode() == HttpStatus.OK){
                return Objects.requireNonNull(JSON.parseArray(response.getBody())).toJavaList(JSONObject.class);
            }
            else return null;
        }
        catch (HttpClientErrorException e){
            if(e.getStatusCode() == HttpStatus.NOT_MODIFIED){
                System.out.println("304 ERROR");
            }
            return null;
        }
    }
}
