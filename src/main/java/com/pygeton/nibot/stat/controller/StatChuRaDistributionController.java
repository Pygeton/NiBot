package com.pygeton.nibot.stat.controller;

import com.alibaba.fastjson.JSON;
import com.pygeton.nibot.stat.pojo.StatChuRaDistribution;
import com.pygeton.nibot.stat.service.StatChuRaDistributionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chu")
@ServletComponentScan
public class StatChuRaDistributionController {

    @Autowired
    StatChuRaDistributionServiceImpl statChuRaDistributionService;

    @RequestMapping("/rating")
    @CrossOrigin(origins = "*")
    public String page(){
        List<StatChuRaDistribution> list = statChuRaDistributionService.list();
        return JSON.toJSONString(list.get(list.size() - 1));
    }
}
