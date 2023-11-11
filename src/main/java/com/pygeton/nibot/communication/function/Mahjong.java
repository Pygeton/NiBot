package com.pygeton.nibot.communication.function;

import com.alibaba.fastjson.JSONObject;
import com.pygeton.nibot.communication.entity.Message;
import com.pygeton.nibot.communication.entity.Params;
import com.pygeton.nibot.communication.entity.Request;
import com.pygeton.nibot.communication.event.IMessageEvent;
import com.pygeton.nibot.communication.websocket.Client;
import com.pygeton.nibot.repository.service.MahjongDataServiceImpl;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class Mahjong implements IMessageEvent {

    Message message;
    String[] rawMessage;
    Request<Params> request;
    Params params;

    @Autowired
    MahjongDataServiceImpl mahjongDataService;

    @Override
    public int weight() {
        return 15;
    }

    @Override
    public boolean onMessage(Message message) {
        this.message = message;
        rawMessage = message.getRaw_message().split(" ");
        if(rawMessage[0].equals("/mj")){
            params = new Params(message);
            match();
            request = new Request<>("send_msg", params);
            System.out.println(JSONObject.toJSONString(request));
            Client.sendMessage(JSONObject.toJSONString(request));
            return true;
        }
        else return false;
    }

    private void match(){
        if(rawMessage.length > 1){
            switch (rawMessage[1]){
                case "bind" -> {
                    if(rawMessage.length == 3){
                        bind(message.getUser_id(), rawMessage[2]);
                    }
                    else {
                        params.addTextMessageSegment("参数有误，请使用/help查看使用说明！");
                    }
                }
                case "rate" -> {
                    if(rawMessage.length == 2){
                        rate(message.getUser_id());
                    }
                    else if(rawMessage.length == 3){
                        //待补全
                        params.addTextMessageSegment("目前还不支持查询他人战绩QAQ");
                    }
                    else {
                        params.addTextMessageSegment("参数有误，请使用/help查看使用说明！");
                    }
                }
            }
        }
        else {
            params.addTextMessageSegment("参数缺失，请使用/help查看使用说明！");
        }
    }

    private void bind(Long user_id,String url){
        if(url.contains("https://rate.000.mk/chart/?name=")){
            boolean ret = mahjongDataService.saveOrUpdateUrl(user_id,url);
            if(ret){
                params.addTextMessageSegment("公式战信息绑定成功！");
            }
            else{
                params.addTextMessageSegment("公式战信息绑定失败：数据库异常");
            }
        }
        else {
            params.addTextMessageSegment("公式战信息绑定失败：URL有误");
        }
    }

    private void rate(Long user_id){
        String url = mahjongDataService.getUrl(user_id);
        System.setProperty("webdriver.chrome.driver","D:/IDE-Enviroment/chromedriver-win64/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        WebDriver driver = new ChromeDriver(options);
        driver.get(url);
        try{
            Thread.sleep(5000);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        String date = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date());
        String fileName = "mj-" + user_id + " " + date + ".png";
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(screenshot,new File("D:/Documents/leidian9/Pictures/Mahjong/" + fileName));
        }
        catch (IOException e){
            e.printStackTrace();
        }
        driver.quit();
        String path = "file:///sdcard/Pictures/Mahjong/" + fileName;
        params.addImageMessageSegment(path);
    }
}
