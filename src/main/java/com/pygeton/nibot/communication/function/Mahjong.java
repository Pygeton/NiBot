package com.pygeton.nibot.communication.function;

import com.pygeton.nibot.communication.entity.Message;
import com.pygeton.nibot.communication.entity.data.AtData;
import com.pygeton.nibot.communication.entity.data.MessageData;
import com.pygeton.nibot.communication.entity.params.SendMsgParams;
import com.pygeton.nibot.communication.event.IMessageEvent;
import com.pygeton.nibot.repository.entity.MahjongData;
import com.pygeton.nibot.repository.service.MahjongDataServiceImpl;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

@Component
public class Mahjong extends Function implements IMessageEvent {

    private enum Mode { BIND, RATE }

    @Autowired
    MahjongDataServiceImpl mahjongDataService;

    @Override
    public int weight() {
        return 50;
    }

    @Override
    public boolean onMessage(Message message) {
        setRawMessage(message);
        if(rawMessage[0].equals("/mj")){
            sendMsgParams = new SendMsgParams(message);
            match(message);
            sendMessage();
            return true;
        }
        else return false;
    }

    private void match(Message message){
        if(rawMessage.length > 1){
            switch (rawMessage[1]){
                case "bind" -> {
                    if(rawMessage.length == 3){
                        bind(message.getUserId(), rawMessage[2]);
                    }
                    else if(rawMessage.length == 4){
                        bind(message.getUserId(), rawMessage[2], Integer.valueOf(rawMessage[3]));
                    }
                    else {
                        sendMsgParams.addTextMessageSegment("参数有误，请输入/help 3查看帮助文档>_<");
                    }
                }
                case "rate" -> {
                    if(rawMessage.length == 2){
                        rate(message.getUserId());
                    }
                    else if(rawMessage.length == 3){
                        if(message.getMessageType().equals("group")){
                            MessageData messageData = message.getSegmentList().get(1).getData();
                            if(messageData instanceof AtData atData){
                                rate(atData.getQq());
                            }
                            else {
                                sendMsgParams.addTextMessageSegment("参数有误，请输入/help 3查看帮助文档>_<");
                            }
                        }
                        else sendMsgParams.addTextMessageSegment("这个功能只有在群聊里才能使用哦QAQ");
                    }
                    else {
                        sendMsgParams.addTextMessageSegment("参数有误，请输入/help 3查看帮助文档>_<");
                    }
                }
            }
        }
        else {
            sendMsgParams.addTextMessageSegment("参数缺失，请输入/help 3查看帮助文档>_<");
        }
    }

    private void bind(Long id,String name){
        String url = "https://rate.000.mk/chart/?name=" + name;
        WebDriver driver = initDriver(url,Mode.BIND);
        boolean alert = alertCheck(driver);
        if(!alert){
            boolean ret = mahjongDataService.saveOrUpdateData(id, name);
            if(ret){
                sendMsgParams.addTextMessageSegment("公式战信息绑定成功！");
            }
            else{
                sendMsgParams.addTextMessageSegment("公式战信息绑定失败：数据库异常");
            }
        }
        driver.quit();
    }

    private void bind(Long id,String name,Integer area){
        String url = "https://rate.000.mk/chart/?name=" + name + "&area=" + area;
        WebDriver driver = initDriver(url,Mode.BIND);
        boolean alert = alertCheck(driver);
        if(!alert){
            boolean ret = mahjongDataService.saveOrUpdateData(id, name, area);
            if(ret){
                sendMsgParams.addTextMessageSegment("公式战信息绑定成功！");
            }
            else{
                sendMsgParams.addTextMessageSegment("公式战信息绑定失败：数据库异常");
            }
        }
        driver.quit();
    }

    private void rate(Long id){
        MahjongData data;
        if(mahjongDataService.getData(id) == null){
            sendMsgParams.addTextMessageSegment("公式战战绩查询失败：用户未绑定");
        }
        else {
            data = mahjongDataService.getData(id);
            String url = "https://rate.000.mk/chart/?name=" + data.getName();
            if(data.getArea() != null){
                url += "&area=" + data.getArea();
            }
            WebDriver driver = initDriver(url,Mode.RATE);
            boolean alert = alertCheck(driver);
            if(!alert){
                String date = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date());
                String fileName = "mj-" + id + " " + date + ".png";
                File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                try {
                    FileUtils.copyFile(screenshot,new File("D:/Documents/leidian9/Pictures/Mahjong/" + fileName));
                }
                catch (IOException e){
                    e.printStackTrace();
                }
                String path = "file:///sdcard/Pictures/Mahjong/" + fileName;
                sendMsgParams.addImageMessageSegment(path);
            }
            driver.quit();
        }


    }

    private WebDriver initDriver(String url, Mode mode){
        System.setProperty("webdriver.chrome.driver","D:/IDE-Enviroment/chromedriver-win64/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        WebDriver driver = new ChromeDriver(options);
        driver.get(url);
        if(mode == Mode.RATE){
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("image_loaded")));
        }
        return driver;
    }

    private boolean alertCheck(WebDriver driver){
        try {
            Alert alert = driver.switchTo().alert();
            sendMsgParams.addTextMessageSegment("发生错误，请输入/help 3查看帮助文档>_<\n");
            sendMsgParams.addTextMessageSegment(alert.getText());
            return true;
        }
        catch (NoAlertPresentException e){
            e.printStackTrace();
            return false;
        }
    }
}
