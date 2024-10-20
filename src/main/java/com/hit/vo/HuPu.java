package com.hit.vo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
/**
 * 首页展示用
 */
public class HuPu {
    /**
     * 帖子的链接
     */
    private String url;
    /**
     * 帖子的标题
     */
    private String title;
    /**
     * 帖子的点赞量
     */
    private String lightNum;
    /**
     * 帖子的回复量
     */
    private String replies;
    /**
     * 帖子的类目
     */
    private String label;
}
