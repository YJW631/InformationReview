package com.hit.mapper;

import com.hit.vo.Review;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentMapper {


    @Select("SELECT pid, review FROM irp.comment WHERE attribution=#{hupuId} AND (#{nowMills} - update_time) < 3600000")
    List<Review> getComments(@Param("hupuId") String hupuId, @Param("nowMills") long nowMills);

    @Insert("insert into irp.comment(attribution,topic_id, pid, review, update_time) VALUES (#{hupuId},#{topicId},#{pid},#{review},#{nowMills})")
    void insert(@Param("hupuId") String hupuId, @Param("topicId") String topicId, @Param("pid") String pid, @Param("review") String review, @Param("nowMills") long nowMills);

    @Select("select distinct topic_id from irp.comment where attribution=#{hupuId}")
    String selectTopicId(String hupuId);
}
