package com.pygeton.nibot.entity;

import lombok.Data;

@Data
public class Sender {

    private Long user_id;
    private String nickname;
    private String card;
    private String role;
    private String title;
    private Integer level;
}
