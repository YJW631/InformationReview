package com.hit.service;

import com.hit.pojo.Result;
import com.hit.vo.Comment;
import com.hit.vo.HuPuList;
import com.hit.vo.UserReview;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public interface SpiderService{
    Result getHotTitle();//获取热榜信息

    Comment getComments(String hupuId);//根据帖子id获取评论

    Map<String, Integer> getInterestedField(String userId);

    List<UserReview> getUserComment(String userId);
}
