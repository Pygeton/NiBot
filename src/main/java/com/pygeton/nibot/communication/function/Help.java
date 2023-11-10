package com.pygeton.nibot.communication.function;

import com.alibaba.fastjson.JSONObject;
import com.pygeton.nibot.communication.entity.Message;
import com.pygeton.nibot.communication.entity.Params;
import com.pygeton.nibot.communication.entity.Request;
import com.pygeton.nibot.communication.event.IMessageEvent;
import com.pygeton.nibot.communication.websocket.Client;
import org.springframework.stereotype.Component;

@Component
public class Help implements IMessageEvent {

    @Override
    public int weight() {
        return 20;
    }

    @Override
    public boolean onMessage(Message message) {
        String[] rawMessage = message.getRaw_message().split(" ");
        if(rawMessage[0].equals("/help")){
            Request<Params> request = new Request<>();
            request.setAction("send_msg");
            Params params = new Params();
            params.setUser_id(message.getUser_id());
            params.setGroup_id(message.getGroup_id());
            params.setMessage_type(message.getMessage_type());
            if(rawMessage.length > 1){
                params.setMessage(match(Integer.parseInt(rawMessage[1])));
            }
            else params.setMessage(match(0));
            params.setAuto_escape(false);
            request.setParams(params);
            System.out.println(JSONObject.toJSONString(request));
            Client.sendMessage(JSONObject.toJSONString(request));
            return true;
        }
        else return false;
    }

    private String match(int param){
        switch (param){
            case 1 -> {
                return """
                        使用方法：
                        直接输入/luck即可，后续将支持每日运势值固定。
                        """;
            }
            case 2 -> {
                return """
                        使用方法：
                        /choose [参数1] [参数2]...[参数n]
                        每个参数之间以空格分割，发送后镍酱就会帮你做出选择！
                        """;
            }
            case 3 -> {
                return """
                        使用方法：
                        1.绑定公式战账号
                        /mj bind [参数]
                        参数内填写公式战的个人战绩的网页URL即可完成绑定，公式战官网为https://rate.000.mk，进入官网后通过右上角的检索可以搜索到自己的个人战绩，复制个人战绩地址栏中的的URL即可。后续无需绑定。
                        2.查询战绩
                        /mj rate [参数(可选)]
                        参数内@其他群友即可查询其战绩（前提是已经绑定），不填写参数则是查询自己的战绩。
                        """;
            }
            default -> {
                return """
                        NiBot现在支持的功能如下：
                        0./help 帮助文档
                        1./luck 今日运势
                        2./choose 做选择
                        3./mj 雀庄公式战（暂未实现）
                        可以通过输入”/help [序号]“查看某项功能的具体使用方法QAQ
                        """;
            }
        }
    }
}
