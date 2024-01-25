package com.pygeton.nibot;

import com.pygeton.nibot.communication.websocket.WebSocketClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Start implements CommandLineRunner {

    @Override
    public void run(String... args) throws IOException {
        //String[] cmd = {"cmd.exe", "/c", "D:/Codeworks/Java/NiBot/bat/NiBotEVM-vue-Start.bat"};
        //Runtime.getRuntime().exec(cmd);
        if(!WebSocketClient.connect()){
            WebSocketClient.reconnect();
        }
    }
}
