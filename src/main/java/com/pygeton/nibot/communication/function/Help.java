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

    Request<Params> request;
    Params params;

    @Override
    public int weight() {
        return 100;
    }

    @Override
    public boolean onMessage(Message message) {
        String[] rawMessage = message.getRaw_message().split(" ");
        if(rawMessage[0].equals("/help")){
            params = new Params(message);
            String text;
            if(rawMessage.length > 1){
                text = match(Integer.parseInt(rawMessage[1]));
            }
            else {
                text = match(0);
            }
            params.addTextMessageSegment(text);
            request = new Request<>("send_msg", params);
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
                        /luck [参数(可选)]
                        直接输入/luck即可查询自己的今日运势，在参数内@群友就可以查询他的今日运势，每日运势值固定。
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
                        /mj bind [参数1] [参数2(可选)]
                        参数1内填写公式战的登记名称即可完成绑定，若提示错误并显示多重结果，请根据信息在参数2内填写符合自己所在区域的区域编号。后续无需绑定。
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
                        3./mj 雀庄公式战
                        可以通过输入”/help [序号]“查看某项功能的具体使用方法QAQ
                        """;
            }
        }
    }
}
