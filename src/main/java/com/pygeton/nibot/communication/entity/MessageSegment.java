package com.pygeton.nibot.communication.entity;

import com.pygeton.nibot.communication.entity.data.MessageData;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageSegment {

    private String type;
    private MessageData data;
}
