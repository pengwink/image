package com.example.springboot.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.springboot.entity.Operation;
import com.example.springboot.entity.Record;
import com.example.springboot.entity.User;
import com.example.springboot.service.RecordService;
import com.example.springboot.utils.TokenUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/record")
public class RecordController {
    @Resource
    RecordService recordService;
    @RequestMapping("/recordDownload")
    public JSONObject recordDownload(HttpServletRequest req, String token,Integer number){
        JSONObject jsonObject = new JSONObject();
        recordService.addRecord(req, Operation.downloadImage.getName(),number, getUser().getId());
        jsonObject.put("status","success");
        return jsonObject;
    }

    @RequestMapping("/selectAll")
    public JSONObject selectAllRecord(String token) throws ParseException {
        JSONObject jsonObject = new JSONObject();
        List<Record> records = recordService.selectAllRecord(getUser().getId());
        jsonObject.put("status","success");
        jsonObject.put("data",records);
        System.out.println(records);
        System.out.println(jsonObject);
        return jsonObject;
    }


    @RequestMapping("/deleteAll")
    public JSONObject deleteAllRecord(String token){
        JSONObject jsonObject = new JSONObject();
        recordService.deleteAllRecord(getUser().getId());
        jsonObject.put("status","success");
        return jsonObject;
    }

    @RequestMapping("/deleteById")
    public JSONObject deleteRecordById(String token,@RequestParam("Ids")List<Integer> Ids){
        JSONObject jsonObject = new JSONObject();
        recordService.deleteRecordByIds(getUser().getId(),Ids);
        jsonObject.put("status","success");
        return jsonObject;
    }
    private User getUser() {
        return TokenUtils.getCurrentUser();
    }
}
