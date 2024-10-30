package com.hit.controller;

import com.hit.pojo.Result;
import com.hit.service.SensitiveService;
import com.hit.vo.HuPuList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class warnController {

    @Autowired
    private SensitiveService sensitiveService;

    @PostMapping("/add")
    public Result addSensitiveWords(@RequestBody Map<String, List<String>> payload) {
        List<String> sensitiveWords = payload.get("words");
        return Result.success(sensitiveService.add(sensitiveWords));
    }

    @DeleteMapping("/sub")
    public Result subWords(@RequestParam(name = "length") int length) {
        return Result.success(sensitiveService.sub(length));
    }
}