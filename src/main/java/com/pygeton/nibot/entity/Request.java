package com.pygeton.nibot.entity;

import lombok.Data;

@Data
public class Request<T> {

    private String action;
    private T params;
    private String echo;
}
