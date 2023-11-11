package com.pygeton.nibot.communication.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Request<T> {

    private String action;
    private T params;
    private String echo;

    public Request(String action,T params){
        this.action = action;
        this.params = params;
    }
}
