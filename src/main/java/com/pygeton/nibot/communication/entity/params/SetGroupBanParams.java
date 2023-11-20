package com.pygeton.nibot.communication.entity.params;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SetGroupBanParams {

    private Long group_id;
    private Long user_id;
    private Long duration;
}
