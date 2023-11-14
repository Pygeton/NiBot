package com.pygeton.nibot.communication.task;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class RescueTask {

    @PostConstruct
    public void init(){
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(this::start, 0, 30, TimeUnit.MINUTES);
    }

    private void start(){
        try {
            String[] cmd = {"cmd.exe", "/c", "D:/Codeworks/Java/NiBot/bat/NiBotEVM-Start.bat"};
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
