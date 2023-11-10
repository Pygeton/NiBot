package com.pygeton.nibot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
@MapperScan("com.pygeton.nibot.repository.mapper")
public class NiBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(NiBotApplication.class, args);
    }

}

