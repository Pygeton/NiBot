package com.pygeton.nibot.communication.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pygeton.nibot.communication.entity.chuni.ChunithmPayload;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class ChunithmHttpService {

    private final RestTemplate restTemplate;
    private final String baseUrl = "https://www.diving-fish.com";

    public ChunithmHttpService(RestTemplateBuilder builder){
        this.restTemplate = builder.build();
    }

    public List<JSONObject> getMusicDataFromSega(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange("https://chunithm.sega.jp/storage/json/music.json", HttpMethod.GET, entity, String.class);
            if(response.getStatusCode() == HttpStatus.OK){
                return Objects.requireNonNull(JSON.parseArray(response.getBody())).toJavaList(JSONObject.class);
            }
            else return null;
        }
        catch (HttpClientErrorException e){
            System.out.println(e.getStatusCode());
            return null;
        }
    }

    public List<JSONObject> getMusicDataFromDivingFish(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(baseUrl + "/api/chunithmprober/music_data", HttpMethod.GET, entity, String.class);
            if(response.getStatusCode() == HttpStatus.OK){
                return Objects.requireNonNull(JSON.parseArray(response.getBody())).toJavaList(JSONObject.class);
            }
            else return null;
        }
        catch (HttpClientErrorException e){
            System.out.println(e.getStatusCode());
            return null;
        }
    }

    public Map<String, List<JSONObject>> getB30AndR10(Long userId){
        Map<String, List<JSONObject>> map = new HashMap<>();
        ChunithmPayload payload = new ChunithmPayload(userId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(JSON.toJSONString(payload),headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(baseUrl + "/api/chunithmprober/query/player", HttpMethod.POST, entity, String.class);
            if(response.getStatusCode() == HttpStatus.OK){
                JSONObject object = JSONObject.parseObject(response.getBody());
                List<JSONObject> userdata = new ArrayList<>();
                JSONObject userObject = new JSONObject();
                userObject.put("nickname",object.getString("nickname"));
                userObject.put("rating",object.getDoubleValue("rating"));
                userdata.add(userObject);
                List<JSONObject> b30 = object.getJSONObject("records").getJSONArray("b30").toJavaList(JSONObject.class);
                List<JSONObject> r10 = object.getJSONObject("records").getJSONArray("r10").toJavaList(JSONObject.class);
                map.put("userdata",userdata);
                map.put("b30",b30);
                map.put("r10",r10);
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
