package com.pygeton.nibot.communication.event;

import com.pygeton.nibot.communication.entity.Message;

public interface IMessageEvent {

    int weight();

    boolean onMessage(Message message);
}
