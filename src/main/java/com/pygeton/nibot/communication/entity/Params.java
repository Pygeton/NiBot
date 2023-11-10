package com.pygeton.nibot.communication.entity;

import lombok.Data;

@Data
public class Params {

    private String message_type;
    private Long user_id;
    private Long group_id;
    private String message;
    private Boolean auto_escape;
}
