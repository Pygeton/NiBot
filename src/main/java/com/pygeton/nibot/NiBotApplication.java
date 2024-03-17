package com.pygeton.nibot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.pygeton.nibot.repository.mapper")
@MapperScan("com.pygeton.nibot.stat.mapper")
@EnableScheduling
public class NiBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(NiBotApplication.class, args);
    }

}

