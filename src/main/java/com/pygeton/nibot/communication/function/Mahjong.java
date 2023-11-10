package com.pygeton.nibot.communication.function;

import com.alibaba.fastjson.JSONObject;
import com.pygeton.nibot.communication.entity.Message;
import com.pygeton.nibot.communication.entity.Params;
import com.pygeton.nibot.communication.entity.Request;
import com.pygeton.nibot.communication.event.IMessageEvent;
import com.pygeton.nibot.communication.websocket.Client;
import com.pygeton.nibot.repository.service.MahjongDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Mahjong implements IMessageEvent {

    @Autowired
    MahjongDataServiceImpl mahjongDataService;

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
            match(message,rawMessage,params);
            params.setAuto_escape(false);
            request.setParams(params);
            System.out.println(JSONObject.toJSONString(request));
            Client.sendMessage(JSONObject.toJSONString(request));
            return true;
        }
        else return false;
    }

    private void match(Message message,String[] rawMessage,Params params){
        if(rawMessage.length > 1){
            switch (rawMessage[1]){
                case "bind" -> {
                    if(rawMessage.length == 3){
                        bind(message.getUser_id(), rawMessage[2],params);
                    }
                    else {
                        params.setMessage("参数有误，请使用/help查看使用说明。");
                    }
                }
                case "rate" -> {
                    //待补全
                }
            }
        }
        else {
            params.setMessage("参数缺失，请使用/help查看使用说明！");
        }
    }

    private void bind(Long user_id,String url,Params params){
        if(url.contains("https://rate.000.mk/chart/?name=")){
            boolean ret = mahjongDataService.saveOrUpdateUrl(user_id,url);
            if(ret){
                params.setMessage("公式战信息绑定成功！");
            }
            else{
                params.setMessage("公式战信息绑定失败：数据库异常");
            }
        }
        else params.setMessage("公式战信息绑定失败：URL有误");
    }

    private void rate(Message message,Params params){

    }
}
