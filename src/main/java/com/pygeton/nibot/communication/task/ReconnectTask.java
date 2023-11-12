package com.pygeton.nibot.communication.task;

import com.pygeton.nibot.communication.websocket.Client;

public class ReconnectTask implements Runnable{

    @Override
    public void run() {
        while (true){
            System.out.println("开始请求重连");
            if(Client.connect("ws://127.0.0.1:9099")){
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
