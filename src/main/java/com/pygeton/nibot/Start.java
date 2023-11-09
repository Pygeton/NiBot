package com.pygeton.nibot;

import com.pygeton.nibot.websocket.Client;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Start implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        Client.connect("ws://127.0.0.1:9099");
    }
}
