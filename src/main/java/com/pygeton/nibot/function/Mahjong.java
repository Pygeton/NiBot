package com.pygeton.nibot.function;

import com.alibaba.fastjson.JSONObject;
import com.pygeton.nibot.entity.Message;
import com.pygeton.nibot.entity.Params;
import com.pygeton.nibot.entity.Request;
import com.pygeton.nibot.event.IMessageEvent;
import com.pygeton.nibot.websocket.Client;
import org.springframework.stereotype.Component;

@Component
public class Mahjong implements IMessageEvent {

    @Override
    public int weight() {
        return 15;
    }

    @Override
    public boolean onMessage(Message message) {
        String[] rawMessage = message.getRaw_message().split(" ");
        if(rawMessage[0].equals("/mj")){
            Request<Params> request = new Request<>();
            request.setAction("send_msg");
            Params params = new Params();
            params.setUser_id(message.getUser_id());
            params.setGroup_id(message.getGroup_id());
            params.setMessage_type(message.getMessage_type());
            params.setMessage(match(message,rawMessage));
            params.setAuto_escape(false);
            request.setParams(params);
            System.out.println(JSONObject.toJSONString(request));
            Client.sendMessage(JSONObject.toJSONString(request));
            return true;
        }
        else return false;
    }

    private String match(Message message,String[] rawMessage){
        int retcode = 0;
        switch (rawMessage[1]){
            case "bind" -> {
                if(rawMessage.length < 3) retcode = 10;
                else retcode = bind(message.getUser_id(), rawMessage[2]);
            }
            case "rate" -> retcode = rate(message);
        }
        switch (retcode){
            case 1 -> {
                return "公式战信息绑定成功！";
            }
            case 2 -> {
                return "此功能尚未实现！";
            }
            case 10 -> {
                return "公式战信息绑定失败：参数缺失";
            }
            case 11 -> {
                return "公式战信息绑定失败：数据库连接异常";
            }
            case 12 -> {
                return "公式战信息绑定失败：用户名不存在";
            }
            case 21 -> {
                return "公式战战绩查询失败：服务器异常";
            }
            case 22 -> {
                return "公式战战绩查询失败：用户名不存在";
            }
            default -> {
                return "请输入正确的参数，可以使用/help查看使用说明！";
            }
        }
    }

    private int bind(Long user_id,String user_name){
        return 2;
    }

    private int rate(Message message){
        return 2;
    }
}
