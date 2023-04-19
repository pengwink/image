package com.example.springboot.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @program: MemoryBack
 * @description 记录操作行为
 * @author: xiaohua
 * @create: 2022-09-02 14:00
 **/
public class Record {

    private Integer id;
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date date;
    private String week;
    private String operation;
    private Integer number;
    private String ipv4;
    private Integer userId;

    public Record(Integer id, Date date, String week, String operation, Integer number, String ipv4, Integer userId) {
        this.id = id;
        this.date = date;
        this.week = week;
        this.operation = operation;
        this.number = number;
        this.ipv4 = ipv4;
        this.userId = userId;
    }

    public Record() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getIpv4() {
        return ipv4;
    }

    public void setIpv4(String ipv4) {
        this.ipv4 = ipv4;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", date=" + date +
                ", week='" + week + '\'' +
                ", operation='" + operation + '\'' +
                ", number=" + number +
                ", ipv4='" + ipv4 + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
