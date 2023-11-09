package com.pygeton.nibot.entity;

import lombok.Data;

@Data
public class Message {

    private Long time;
    private String self_id;
    private String post_type;
    private String message_type;
    private String meta_event_type;
    private String sub_type;
    private Long message_id;
    private Long group_id;
    private Long peer_id;
    private Long user_id;
    private String message;
    private String raw_message;
    private Integer font;
    private Sender sender;
}
