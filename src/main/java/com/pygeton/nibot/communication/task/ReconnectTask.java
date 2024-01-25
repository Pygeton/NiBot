package com.pygeton.nibot.communication.task;

import com.pygeton.nibot.communication.websocket.WebSocketClient;

public class ReconnectTask implements Runnable{

    @Override
    public void run() {
        while (true){
            System.out.println("开始请求重连");
            if(WebSocketClient.connect()){
                break;
            }
            else {
                try {
                    System.out.println("即将开始下一次重连");
                    Thread.sleep(2000);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public static void execute(){
        new Thread(new ReconnectTask()).start();
    }
}
