package me.tianshuang.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface Mapper1 {

    @Select("SELECT 1")
    int select1();

}
