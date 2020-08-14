package com.hnsh.dialogue.utils;

import java.util.Calendar;
import java.util.TimeZone;

public class DateUtil {
    private static volatile DateUtil instance = null;    //保证 instance 在所有线程中同步

    private DateUtil() {
    }    //private 避免类在外部被实例化

    public static synchronized DateUtil getInstance() {
        //getInstance 方法前加同步
        if (instance == null) {
            instance = new DateUtil();
        }
        return instance;
    }

    public String getTime() {
        String res = "";
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mYear = String.valueOf(calendar.get(Calendar.YEAR));           // 获取当前年份
        String mMonth = String.valueOf(calendar.get(Calendar.MONTH) + 1);     // 获取当前月份
        String mDay = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));    // 获取当前月份的日期号码
        String mWay = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));     //获取当前周几
        String mHour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));    //获取当前小时
        String mMinute = String.valueOf(calendar.get(Calendar.MINUTE));       //获取当前多少分
        if ("1".equals(mWay)) {
            mWay = "日";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }
        if (mHour.length() < 2) {
            mHour = "0" + mHour;
        }
        if (mMinute.length() < 2) {
            mMinute = "0" + mMinute;
        }
        res = mYear + "/" + mMonth + "/" + mDay + "," + mHour + ":" + mMinute + ",星期" + mWay;
        return res;
    }
}
