package com.pygeton.nibot.communication.function;

import com.pygeton.nibot.communication.entity.Message;
import com.pygeton.nibot.communication.entity.params.SendMsgParams;
import com.pygeton.nibot.communication.event.IMessageEvent;
import org.springframework.stereotype.Component;

@Component
public class Help extends Function implements IMessageEvent {

    @Override
    public int weight() {
        return 100;
    }

    @Override
    public boolean onMessage(Message message) {
        setRawMessage(message);
        if(rawMessage[0].equals("/help")){
            sendMsgParams = new SendMsgParams(message);
            String text;
            if(rawMessage.length > 1){
                text = match(message,Integer.parseInt(rawMessage[1]));
            }
            else {
                text = match(message,0);
            }
            sendMsgParams.addTextMessageSegment(text);
            sendMessage();
            return true;
        }
        else return false;
    }

    private String match(Message message,int param){
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
            case 4 -> {
                if(message.getGroup_id() != null){
                    if(message.getGroup_id() == 251697087L)
                    {
                        return """
                        使用方法：
                        1.召唤随机龙图
                        /long
                        可以召唤一张随机的龙图.jpg
                        2.添加龙图
                        [图片]+回复：/long add
                        发送一张你想要添加的龙图，然后对龙图回复并输入上述指令，审核通过后你贡献的龙图就会出现在随机池里！
                        【审核群限定】其他群发出的龙图添加请求会被转发到此群，若认定通过审核，直接回复该龙图并输入上述指令即可将此龙图加入随机召唤池。同理，在本群直接发起添加操作无需审核，视为直接通过。
                        """;
                    }
                    else {
                        return """
                        使用方法：
                        1.召唤随机龙图
                        /long
                        可以召唤一张随机的龙图.jpg
                        2.添加龙图
                        [图片]+回复：/long add
                        发送一张你想要添加的龙图，然后对龙图回复并输入上述指令，审核通过后你贡献的龙图就会出现在随机池里！
                        """;
                    }
                }
                else {
                    return """
                        使用方法：
                        1.召唤随机龙图
                        /long
                        可以召唤一张随机的龙图.jpg
                        2.添加龙图
                        [图片]+回复：/long add
                        发送一张你想要添加的龙图，然后对龙图回复并输入上述指令，审核通过后你贡献的龙图就会出现在随机池里！
                        """;
                }
            }
            case 5 -> {
                return """
                        使用方法：
                        /sleep [参数]
                        参数内填写你想要睡眠的时长，单位为时（例如：/sleep 8 = 禁言8小时），发送指令后镍酱就会不让你说话直到你睡醒为止！
                        如果你提前睡醒想说话，需要私聊其他管理员（
                        """;
            }
            case 6 -> {
                return "此功能暂未实现，敬请期待。";
            }
            default -> {
                return """
                        NiBot现在支持的功能如下：
                        0./help 帮助文档
                        1./luck 今日运势
                        2./choose 做选择
                        3./mj 雀庄公式战
                        4./long 召唤龙图
                        5./sleep 精致睡眠
                        6./mai 舞萌DX（暂未实现）
                        可以通过输入”/help [序号]“查看某项功能的具体使用方法QAQ
                        例如：输入/help 1，可以查看今日运势的功能详情。
                        """;
            }
        }
    }
}
