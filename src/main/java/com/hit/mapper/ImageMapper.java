package com.hit.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ImageMapper {
    @Select("select url from irp.image where pid=#{hupuId};")
    List<String> getImageUrls(String hupuId);

    @Insert("insert into irp.image (pid, url) values (#{hupuId},#{imgUrl})")
    void insert(@Param("hupuId") String hupuId, @Param("imgUrl") String imgUrl);
}
