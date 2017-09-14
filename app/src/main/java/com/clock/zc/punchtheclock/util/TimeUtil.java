package com.clock.zc.punchtheclock.util;

import android.text.TextUtils;
import android.text.format.Time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Zc on 2017/8/28.
 */

public class TimeUtil {
    private static String mYear; // 当前年
    private static String mMonth; // 月
    private static String mHour;
    private static String mDay;
    private static String mMinute;
    private static String mWay;
    //利用Calendar获取
    public static void calendarTime(){
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);//取得系统日期:
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);//取得系统时间：
        int minute = c.get(Calendar.MINUTE);

        Calendar c1 = Calendar.getInstance();
        year = c1.get(Calendar.YEAR);//取得系统日期:
        month = c1.get(Calendar.MONTH);
        day = c1.get(Calendar.DAY_OF_MONTH);
        hour = c1.get(Calendar.HOUR_OF_DAY);//取得系统时间：
        minute = c1.get(Calendar.MINUTE);
    }
    public static String getData(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    public static String getDataHour(){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }
    /**
     * 获取当前日期几月几号
     */
    public static List<String> getDateListString() {
        List<String> list = new ArrayList<>();
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mYear = String.valueOf(c.get(Calendar.YEAR));
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        mHour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        mMinute = String.valueOf(c.get(Calendar.MINUTE));
        list.add(mYear);
        list.add(mMonth);
        list.add(mDay);
        list.add(mHour);
        list.add(mMinute);
        return list;
    }
    /**
     * 获取当前日期几月几号
     */
    public static List<Integer> getDateListInt() {
        List<Integer> list = new ArrayList<>();
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;// 获取当前月份
        int day = c.get(Calendar.DAY_OF_MONTH);// 获取当前月份的日期号码
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        list.add(year);
        list.add(month);
        list.add(day);
        list.add(hour);
        list.add(minute);
        return list;
    }
    /**
     * 获取当前日期小时
     */
    public static String getDateHour() {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        mHour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        return mHour;
    }
    /**
     * 获取当前年月日
     *
     * @return
     */
    public static String StringData() {

        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mYear = String.valueOf(c.get(Calendar.YEAR));// 获取当前年份
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        return mYear + "-" + mMonth + "-" + mDay;
    }
    /**
     * 获取当前是周几
     *
     */
    public static String getWeekString() {
        final Calendar c = Calendar.getInstance();
        mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWay)) {
            mWay = "周天";
        } else if ("2".equals(mWay)) {
            mWay = "周一";
        } else if ("3".equals(mWay)) {
            mWay = "周二";
        } else if ("4".equals(mWay)) {
            mWay = "周三";
        } else if ("5".equals(mWay)) {
            mWay = "周四";
        } else if ("6".equals(mWay)) {
            mWay = "周五";
        } else if ("7".equals(mWay)) {
            mWay = "周六";
        }
        return mWay;
    }
    public static String getHM(String time){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        if(TextUtils.isEmpty(time)){

            return "";
        }else{
            try {
                Date date = formatter.parse(time);
                String str = format.format(date);
                return str;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return "s";
    }
    public static String getYMD(String time){
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        if(TextUtils.isEmpty(time)){
            return "";
        }else{
            try {
                Date date = format.parse(time);
                String str = format.format(date);
                return str;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
    /**
     * 根据当前日期获得是星期几
     *
     * @return
     */
    public static String getWeek(String time) {
        String Week = "";

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {

            c.setTime(format.parse(time));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            Week += "周天";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 2) {
            Week += "周一";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 3) {
            Week += "周二";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 4) {
            Week += "周三";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 5) {
            Week += "周四";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 6) {
            Week += "周五";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 7) {
            Week += "周六";
        }
        return Week;
    }
    public static List<String> get7date() {
        List<String> dates = new ArrayList<String>();
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        java.text.SimpleDateFormat sim = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String date = sim.format(c.getTime());
        dates.add(date);
        for (int i = 0; i < 6; i++) {
            c.add(java.util.Calendar.DAY_OF_MONTH, 1);
            date = sim.format(c.getTime());
            dates.add(date);
        }
        return dates;
    }
    /**
     * 获取今天往后一周的日期（几月几号）
     */
    public static  List<String> getSevendate() {
        List<String > dates = new ArrayList<String>();
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

        for (int i = 0; i < 7; i++) {
            mYear = String.valueOf(c.get(Calendar.YEAR));// 获取当前年份
            mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
            mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH)+i);// 获取当前日份的日期号码
            String date =mMonth + "月" + mDay + "日";
            dates.add(date);
        }
        return dates;
    }
    /**
     * 获取今天往后一周的集合
     */
    public static List<String > get7week(){
        String week="";
        List<String > weeksList = new ArrayList<String>();
        List<String> dateList = get7date();
        for(String s:dateList ){
            if (s.equals(StringData())) {
                week="今天";
            }else {
                week=getWeek(s);
            }
            weeksList.add(week);
        }
        return weeksList;
    }
}
