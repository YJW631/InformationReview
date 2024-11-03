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
     * 帖子所属话题的id
     */
    private String topicId;
    /**
     * 帖子的评论
     * 记录前五十条
     */
    private List<Review> reviews;
}
