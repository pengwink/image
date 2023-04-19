package com.example.springboot.utils;

import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
public class DateUtil {

    public String getWeek(Date date) throws ParseException {
        // 再转换为时间
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String[] weeks = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};;
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int week_index = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (week_index < 0) {
            week_index = 0;
        }
        return weeks[week_index];
    }

}
