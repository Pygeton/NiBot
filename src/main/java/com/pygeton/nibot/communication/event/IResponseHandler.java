package com.pygeton.nibot.communication.event;

import com.pygeton.nibot.communication.entity.Response;

public interface IResponseHandler {

    void handle(Response response);
}
