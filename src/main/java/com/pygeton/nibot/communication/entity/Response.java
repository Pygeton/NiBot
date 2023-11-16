package com.pygeton.nibot.communication.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pygeton.nibot.communication.entity.data.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Response {

    private String status;
    private Integer retcode;
    private String msg;
    private String wording;
    private String data;
    private List<MessageSegment> segmentList = new ArrayList<>();
    private String echo;

    public void toSegmentList(){
        JSONObject dataObject = JSON.parseObject(data);
        JSONArray jsonArray = dataObject.getJSONArray("message");
        for (int i = 0; i < jsonArray.size(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String type = jsonObject.getString("type");
            JSONObject data = jsonObject.getJSONObject("data");
            MessageSegment messageSegment;
            switch (type){
                case "text" -> {
                    messageSegment = new MessageSegment(type,data.toJavaObject(TextData.class));
                    segmentList.add(messageSegment);
                }
                case "image" -> {
                    messageSegment = new MessageSegment(type,data.toJavaObject(ImageData.class));
                    segmentList.add(messageSegment);
                }
                case "face" -> {
                    messageSegment = new MessageSegment(type,data.toJavaObject(FaceData.class));
                    segmentList.add(messageSegment);
                }
                case "at" -> {
                    messageSegment = new MessageSegment(type,data.toJavaObject(AtData.class));
                    segmentList.add(messageSegment);
                }
                case "reply" -> {
                    messageSegment = new MessageSegment(type,data.toJavaObject(ReplyData.class));
                    segmentList.add(messageSegment);
                }
            }
        }
    }
}
