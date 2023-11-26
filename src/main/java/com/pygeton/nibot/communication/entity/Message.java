package com.pygeton.nibot.communication.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pygeton.nibot.communication.entity.data.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Message {

    private Long time;
    private String selfId;
    private String postType;
    private String messageType;
    private String metaEventType;
    private String subType;
    private Long messageId;
    private Long groupId;
    private Long targetId;
    private Long peerId;
    private Long userId;
    private String message;
    private List<MessageSegment> segmentList = new ArrayList<>();
    private String rawMessage;
    private Integer font;
    private Sender sender;

    public void toSegmentList(){
        JSONArray jsonArray = JSON.parseArray(message);
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
