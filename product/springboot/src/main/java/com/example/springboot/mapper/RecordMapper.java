package com.example.springboot.mapper;

import com.example.springboot.entity.Record;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RecordMapper {

    /**
     * 插入一条操作记录
     * @param record
     */
    @Insert("insert into record values (null,#{date},#{operation},#{number},#{ipv4},#{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void addRecord(Record record);

    /**
     * 查询所有记录
     * @param userId
     * @return
     */
    @Select("select * from record where userId=#{userId}")
    List<Record> selectAllRecord(Integer userId);

    /**
     * 删除所有记录
     * @param userId
     */
    @Delete("delete  from record where userId=#{userId}")
    void deleteAllRecord(Integer userId);

    /**
     * 通过记录id删除
     * @param userId
     * @param Ids
     */
    void deleteRecordByIds(Integer userId,@Param("list")List<Integer> Ids);
}
