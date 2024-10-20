package com.hit.controller;

import com.hit.pojo.Result;
import com.hit.service.SpiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @Autowired
    private SpiderService spiderService;

    @GetMapping("/hot")//获取当前虎扑热榜信息
    public Result hupuList() {
        return spiderService.getHotTitle();
    }

}
