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
        scheduledExecutorService.scheduleAtFixedRate(this::startQQ, 0, 30, TimeUnit.MINUTES);
    }

    private void startQQ(){
        try {
            String[] cmd = {"cmd.exe", "/c", "cd D:\\Software\\leidian\\LDPlayer9 && adb -s emulator-5554 shell am start -n com.tencent.mobileqq/.activity.SplashActivity"};
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
