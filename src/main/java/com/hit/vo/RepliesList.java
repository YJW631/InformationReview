package com.hit.vo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
/**
 * 评论
 */
public class RepliesList {
    /**
     * 评论id
     */
    private String pid;
    /**
     * 用户id
     */
    private String authorId;
    /**
     * 内容
     */
    private String content;
    /**
     * 未用
     */
    private Integer count;
    /**
     * 评论的点亮数量
     */
    private Integer allLightCount;
    /**
     * 发布这条评论使用的设备 大多是都是Android
     */
    private String client;
    /**
     * 评论的时间，时间戳
     */
    private String createdAt;
    /**
     * 评论的时间，格式化以后的，比如格式化以后是8小时前
     */
    private String createdAtFormat;
    /**
     * 用户对象，发布这条评论的用户对象
     */
    private Author author;
    /**
     * 回复数量（这太评论有多少条回复量)
     */
    private Integer replyNum;
    /**
     * 未用
     */
    private Integer userBanned;
    /**
     * 未用
     */
    private Integer hidePost;
    /**
     * 未用
     */
    private String attr;
    /**
     * 地点（发布这套评论的地点，比如上海）
     */
    private String location;
    /**
     * 引用（这条评论的引用评论，比如某某用户引用了某某用户的某评论）
     */
    private RepliesList quote;
}
