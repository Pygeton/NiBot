package com.pygeton.nibot.stat.controller;

import com.alibaba.fastjson.JSON;
import com.pygeton.nibot.stat.pojo.StatMaiGradeAvg;
import com.pygeton.nibot.stat.service.StatMaiGradeAvgServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/mai")
@ServletComponentScan
public class StatMaiGradeAvgController {

    @Autowired
    StatMaiGradeAvgServiceImpl statMaiGradeAvgService;

    @RequestMapping("/grade")
    @CrossOrigin(origins = "*")
    public String page(){
        List<StatMaiGradeAvg> list = statMaiGradeAvgService.list();
        return JSON.toJSONString(list.get(list.size() - 1));
    }
}
