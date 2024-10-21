package com.hit.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class Comment {
    /**
     * 帖子的id
     */
    private String hupuId;
    /**
     * 帖子的id
     * 记录前三十条
     */
    private List<String> reviews;
}
