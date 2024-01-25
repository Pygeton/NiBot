package com.pygeton.nibot.communication.task;

import com.alibaba.fastjson.JSON;
import com.pygeton.nibot.communication.entity.Request;
import com.pygeton.nibot.communication.websocket.WebSocketServer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.PrintStream;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class ConsoleTask {

    private final WebSocketServer webSocketServer;
    private final ConsoleOutputCatcher outputCatcher;

    static class ConsoleOutputCatcher {
        private final Queue<String> outputQueue = new ConcurrentLinkedQueue<>();

        public void start() {
            System.setOut(new PrintStream(System.out) {
                public void println(String x) {
                    super.println(x);
                    outputQueue.add(x);
                }
            });
        }

        public String getOutput() {
            return outputQueue.poll();
        }
    }

    public ConsoleTask(WebSocketServer webSocketServer) {
        this.webSocketServer = webSocketServer;
        this.outputCatcher = new ConsoleOutputCatcher();
        this.outputCatcher.start();
    }

    @Scheduled(fixedRate = 1000)
    public void forward(){
        String output = outputCatcher.getOutput();
        if (output != null){
            Request<String> request = new Request<>("console",output);
            webSocketServer.sendMessage(JSON.toJSONString(request));
        }
    }
}
