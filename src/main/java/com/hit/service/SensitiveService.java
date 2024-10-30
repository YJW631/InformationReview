package com.hit.service;

import com.hit.pojo.Result;

import java.util.List;

public interface SensitiveService {
    Result add(List<String> sensitiveWords);

    Result sub(int length);
}
