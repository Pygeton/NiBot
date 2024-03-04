package com.pygeton.nibot.communication.function;

import com.pygeton.nibot.communication.entity.Message;
import com.pygeton.nibot.communication.entity.params.SendMsgParams;
import com.pygeton.nibot.communication.event.IMessageEvent;
import com.pygeton.nibot.repository.entity.AdminData;
import com.pygeton.nibot.repository.service.AdminDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class Admin extends Function implements IMessageEvent {

    @Autowired
    AdminDataService adminDataService;

    @Override
    public int weight() {
        return 999;
    }

    @Override
    public boolean onMessage(Message message) {
        setRawMessage(message);
        if (rawMessage[0].equals("/admin")){
            sendMsgParams = new SendMsgParams(message);
            if(adminDataService.isAdminExist(message.getUserId())){
                match(message);
            }
            else {
                sendMsgParams.addTextMessageSegment("你没有管理员权限，无法使用此功能>_<");
                sendMessage();
            }
            return true;
        }
        return false;
    }

    private void match(Message message){
        switch (rawMessage[1]){
            //超级管理员功能
            case "add" -> addAdmin(message.getUserId());
            case "del" -> deleteAdmin(message.getUserId());
            //普通管理员功能
            case "list" -> getAdminList();
        }
    }

    private void addAdmin(Long userId){
        if(adminDataService.isSuperAdmin(userId)){
            Long id = Long.valueOf(rawMessage[2]);
            if(Objects.equals(userId, id)){
                sendMsgParams.addTextMessageSegment("你不能对自己的账号进行这项操作！");
            }
            else {
                boolean isExist = adminDataService.isAdminExist(id);
                if(isExist){
                    sendMsgParams.addTextMessageSegment("此用户已经成为管理员，无需重复添加！");
                }
                else {
                    if(adminDataService.addAdmin(id)){
                        sendMsgParams.addTextMessageSegment("添加管理员成功！");
                    }
                    else sendMsgParams.addTextMessageSegment("添加管理员失败...");
                }
            }
        }
        else {
            sendMsgParams.addTextMessageSegment("你不是超级管理员，无法使用此功能！");
        }
        sendMessage();
    }

    private void deleteAdmin(Long userId){
        if(adminDataService.isSuperAdmin(userId)){
            Long id = Long.valueOf(rawMessage[2]);
            if(Objects.equals(userId, id)){
                sendMsgParams.addTextMessageSegment("你不能对自己的账号进行这项操作！");
            }
            else {
                boolean isExist = adminDataService.isAdminExist(id);
                if(!isExist){
                    sendMsgParams.addTextMessageSegment("此用户不是管理员，无法进行删除！");
                }
                else {
                    if(adminDataService.deleteAdmin(id)){
                        sendMsgParams.addTextMessageSegment("删除管理员成功！");
                    }
                    else sendMsgParams.addTextMessageSegment("删除管理员失败...");
                }
            }
        }
        else {
            sendMsgParams.addTextMessageSegment("你不是超级管理员，无法使用此功能！");
        }
        sendMessage();
    }

    private void getAdminList(){
        List<AdminData> dataList = adminDataService.getAdminList();
        StringBuilder builder = new StringBuilder("管理员列表如下：\n");
        builder.append("ID | 权限等级\n");
        for (AdminData data : dataList){
            builder.append(data.getId()).append(" | ");
            if (data.getIsSuper()){
                builder.append("超级管理员\n");
            }
            else {
                builder.append("管理员\n");
            }
        }
        sendMsgParams.addTextMessageSegment(builder.toString());
        sendMessage();
    }
}
