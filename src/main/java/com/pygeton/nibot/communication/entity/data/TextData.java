package com.pygeton.nibot.communication.entity.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class TextData extends MessageData {

    private String text;
}
