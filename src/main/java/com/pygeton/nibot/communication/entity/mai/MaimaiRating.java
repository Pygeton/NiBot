package com.pygeton.nibot.communication.entity.mai;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MaimaiRating {

    private Float level;
    private String grade;
    private Integer rating;
}
