package com.hit.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
/**
 * 评论列表对象
 */
public class Replies {
    /**
     * 评论的总条数
     */
    private Integer count;
    /**
     * 一页显示几条帖子
     */
    private Integer size;
    /**
     * 页面索引（当前是第几页）
     */
    private Integer current;
    /**
     * 评论的页数（帖子的评论页数）
     */
    private Integer total;
    /**
     * 帖子的网址
     */
    private String baseUrl;
    /**
     * 评论列表
     */
    private List<RepliesList> list;
}
