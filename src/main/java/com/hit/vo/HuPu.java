package com.hit.vo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class HuPu {//帖子概略信息
    /**
     * 帖子的id
     */
    private String hupuId;
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
    private Integer lightNum;
    /**
     * 帖子的回复量
     */
    private Integer repliesNum;
    /**
     * 帖子的类目
     */
    private String label;
}
