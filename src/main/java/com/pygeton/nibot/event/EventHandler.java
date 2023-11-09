package com.pygeton.nibot.event;

import com.pygeton.nibot.entity.Message;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class EventHandler {

    private static Map<String,IMessageEvent> eventMap;
    private static List<IMessageEvent> eventList;

    @Autowired
    public void setEventMap(Map<String,IMessageEvent> eventMap){
        EventHandler.eventMap = eventMap;
    }

    @PostConstruct
    public void init(){
        eventList = new ArrayList<>(eventMap.size());
        eventList.addAll(eventMap.values());
        eventList = eventList.stream().sorted(Comparator.comparing(IMessageEvent::weight).reversed()).collect(Collectors.toList());
    }

    public static void traverse(Message message){
        for (IMessageEvent event : eventList){
            if(event.onMessage(message)){
                break;
            }
        }
    }
}
