package com.example.springboot.service.impl;

import com.example.springboot.entity.Record;
import com.example.springboot.mapper.RecordMapper;
import com.example.springboot.service.RecordService;
import com.example.springboot.utils.DateUtil;
import com.example.springboot.utils.IpUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Service
public class RecordServiceImpl implements RecordService {

    @Resource
    RecordMapper recordMapper;
    @Resource
    IpUtil ipUtil;
    @Resource
    DateUtil dateUtil;

    /**
     * 添加记录
     * @param operation
     * @param
     * @param number
     * @param userId
     * @return
     */
    @Override
    public boolean addRecord(HttpServletRequest req,String operation, Integer number, Integer userId) {
        Date date = new Date();
        Record record = new Record();
        record.setDate(date);
        record.setOperation(operation);
        record.setNumber(number);
        record.setIpv4(ipUtil.getIpAddress(req));
        record.setUserId(userId);
        recordMapper.addRecord(record);
        if(record.getId()!=null){
            return true;
        }
        return false;
    }

    /**
     * 查询所有记录
     * @param userId
     * @return
     * @throws ParseException
     */
    @Override
    public List<Record> selectAllRecord(Integer userId) throws ParseException {
        List<Record> records = recordMapper.selectAllRecord(userId);
        for(Record r:records){
            String week = dateUtil.getWeek(r.getDate());
            r.setWeek(week);
        }
        return records;
    }

    /**
     * 删除所有记录
     * @param userId
     */
    @Override
    public void deleteAllRecord(Integer userId) {
        recordMapper.deleteAllRecord(userId);
    }

    /**
     * 通过id删除记录
     * @param userId
     * @param Ids
     */
    @Override
    public void deleteRecordByIds(Integer userId, List<Integer> Ids) {
        recordMapper.deleteRecordByIds(userId,Ids);
    }

}
