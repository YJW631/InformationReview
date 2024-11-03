package com.hit.mapper;

import com.hit.vo.UserReview;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserReviewMapper {
    @Select("select userId,review from irp.user_review where userId=#{userId}")
    List<UserReview> getUserReview(String userId);

    @Insert("insert into irp.user_review (userId,review) values (#{userId},#{review});")
    void insert(UserReview userReview);
}
