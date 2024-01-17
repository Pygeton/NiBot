package com.pygeton.nibot.communication.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RescueTask {

    @Scheduled(fixedRate = 1800000)
    public void execute(){
        try {
            String[] cmd = {"cmd.exe", "/c", "D:/Codeworks/Java/NiBot/bat/NiBotEVM-Start.bat"};
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
