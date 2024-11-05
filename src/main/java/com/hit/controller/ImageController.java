package com.hit.controller;

import com.hit.pojo.Result;
import com.hit.service.SpiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ImageController {

    @Autowired
    private SpiderService spiderService;

    @GetMapping("/image")
    public Result getImage(@RequestParam(name = "hupuId") String hupuId) {
        return Result.success(spiderService.getImages(hupuId));
    }

}
