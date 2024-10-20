package com.hit.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@AllArgsConstructor
/**
 * 帖子详情，包括内容 热帖列表 帖子列表 点赞等等
 */
public class HuPuInfo {
    /**
     * 视频封面
     */
    private String videoCover;
    /**
     * 视频地址
     */
    private String videoUrl;
    /**
     * 时间
     */
    private String time;
    /**
     * 时间格式化 如：8小时前
     */
    private String createdAt;
    /**
     * 用户对象
     */
    private Author author;
    /**
     * 地点
     */
    private String location;
    /**
     * 内容
     */
    private String content;
    /**
     * 热帖列表
     */
    private List<RepliesList> lightsList;
    /**
     * 帖子列表
     */
    private Replies repliesObject;
    /**
     * 帖子id
     */
    private String tid;
    /**
     * 帖子标题
     */
    private String title;
    /**
     * 帖子电量次数
     */
    private String lights;
    /**
     * 帖子回复数量
     */
    private String replies;
    /**
     * 帖子的推荐数量
     */
    private String recommend;
    /**
     * 帖子的阅读数量
     */
    private String read;
    /**
     * 客户端 也就是发布这个帖子的用户用的是什么设备发布的，大部分都是Android
     */
    private String client;
}
