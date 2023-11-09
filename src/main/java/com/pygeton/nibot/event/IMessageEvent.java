package com.pygeton.nibot.event;

import com.pygeton.nibot.entity.Message;

public interface IMessageEvent {

    int weight();

    boolean onMessage(Message message);
}
