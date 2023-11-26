package com.pygeton.nibot.communication.entity.params;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SetGroupBanParams {

    private Long groupId;
    private Long userId;
    private Long duration;
}
