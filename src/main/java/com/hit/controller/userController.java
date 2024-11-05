package com.hit.controller;

import com.hit.pojo.Result;
import com.hit.service.SpiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class userController {

    @Autowired
    private SpiderService spiderService;

    @GetMapping("/iField")
    public Result interestedField(@RequestParam(name = "userId") String userId) {
        return Result.success(spiderService.getInterestedField(userId));
    }

    @GetMapping("/summary")
    public Result summaryUser(@RequestParam(name = "userId") String userId) {
        return Result.success(spiderService.getUserComment(userId));
    }

}
