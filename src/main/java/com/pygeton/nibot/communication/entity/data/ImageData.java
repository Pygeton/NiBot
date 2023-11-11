package com.pygeton.nibot.communication.entity.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class ImageData extends MessageData {

    private String file;
    private String url;

    public ImageData(String file){
        this.file = file;
    }
}
