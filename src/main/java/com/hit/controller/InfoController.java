package com.hit.controller;

import com.hit.pojo.Result;
import com.hit.service.SpiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {
    @Autowired
    private SpiderService spiderService;

    @GetMapping("/info")
    public Result Comments(@RequestParam(name = "hupuId") String hupuId) {
        return Result.success(spiderService.getComments(hupuId));
    }


}
