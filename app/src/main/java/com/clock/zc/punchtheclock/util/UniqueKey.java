package com.clock.zc.punchtheclock.util;

/*
 * 所有存在同一个SharedPreference里的值都需要一个唯一的key，
 * 为了保证这些key的唯一性，我们借用就了enum的特性，
 * 用的时候将enum转成String使用即可
 * calarm_math 记录打卡时间
 * calarm_hour 设置打卡时间小时
 * calarm_minute 设置打卡时间分钟
 * work_hour 设置上班时长
 * clock_type 设置打卡方式
 */
public enum UniqueKey {

    calarm_math,calarm_hour,calarm_minute,work_hour,clock_type,

}
