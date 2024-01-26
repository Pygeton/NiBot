package com.pygeton.nibot;

import com.pygeton.nibot.communication.websocket.WebSocketClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

@Component
public class Start implements CommandLineRunner {

    @Override
    public void run(String... args) throws IOException {
        //String[] cmd = {"cmd.exe", "/c", "D:/Codeworks/Java/NiBot/bat/NiBotEVM-vue-Start.bat"};
        //Runtime.getRuntime().exec(cmd);
        loadDll();
        if(!WebSocketClient.connect()){
            WebSocketClient.reconnect();
        }
    }

    public static void loadDll() {
        // 解决awt报错问题
        System.setProperty("java.awt.headless", "false");
        System.out.println(System.getProperty("java.library.path"));
        // 加载动态库
        URL url = ClassLoader.getSystemResource("dlls/opencv_java490.dll");
        System.load(url.getPath());
    }

}
