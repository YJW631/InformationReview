package com.hit.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class HuPuList {//热帖榜每个板块信息
    /**
     * 类目
     */
    private String category;
    /**
     * 首页帖子列表
     */
    private List<HuPu> huPuList;
}
