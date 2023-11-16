package com.pygeton.nibot.communication.entity.params;

import com.pygeton.nibot.communication.entity.Message;
import com.pygeton.nibot.communication.entity.MessageSegment;
import com.pygeton.nibot.communication.entity.data.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SendMsgParams {

    private String message_type;
    private Long user_id;
    private Long group_id;
    private List<MessageSegment> message = new ArrayList<>();
    private Boolean auto_escape = false;

    public SendMsgParams(Message message){
        this.message_type = message.getMessage_type();
        this.user_id = message.getUser_id();
        this.group_id = message.getGroup_id();
    }

    public void addTextMessageSegment(String text){
        TextData data = new TextData(text);
        MessageSegment segment = new MessageSegment("text",data);
        message.add(segment);
    }

    public void addImageMessageSegment(String path){
        ImageData data = new ImageData(path);
        MessageSegment segment = new MessageSegment("image",data);
        message.add(segment);
    }

    public void addImageMessageSegment(String path,String url){
        ImageData data = new ImageData(path,url);
        MessageSegment segment = new MessageSegment("image",data);
        message.add(segment);
    }

    public void addAtMessageSegment(Long qq){
        AtData data = new AtData(qq);
        MessageSegment segment = new MessageSegment("at",data);
        message.add(segment);
    }

    public void addFaceMessageSegment(Integer id){
        FaceData data = new FaceData(id);
        MessageSegment segment = new MessageSegment("face",data);
        message.add(segment);
    }

    public void addReplyMessageSegment(Integer id){
        ReplyData data = new ReplyData(id);
        MessageSegment segment = new MessageSegment("reply",data);
        message.add(segment);
    }

    public void clearMessage(){
        message = new ArrayList<>();
    }
}
