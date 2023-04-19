package com.example.springboot.service;

import com.example.springboot.entity.Record;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;

@Service
public interface RecordService {
    /**
     * 添加记录
     * @param operation
     * @param
     * @param number
     * @param userId
     * @return
     */
    boolean addRecord( HttpServletRequest req,String operation, Integer number, Integer userId);


    /**
     * 查询所有记录
     * @param userId
     * @return
     */
    List<Record> selectAllRecord(Integer userId) throws ParseException;

    /**
     * 删除所有记录
     * @param userId
     */
    void deleteAllRecord(Integer userId);

    /**
     * 删除部分记录
     * @param userId
     * @param Ids
     */
    void deleteRecordByIds(Integer userId,List<Integer> Ids);


}
