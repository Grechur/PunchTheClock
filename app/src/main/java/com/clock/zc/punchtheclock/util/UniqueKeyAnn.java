package com.clock.zc.punchtheclock.util;

import android.support.annotation.StringDef;

/**
 * Created by Zc on 2018/2/11.
 * 所有存在同一个SharedPreference里的值都需要一个唯一的key，
 * 为了保证这些key的唯一性，我们借用就了enum的特性，
 * 用的时候将enum转成String使用即可
 * calarm_math 记录打卡时间
 * calarm_hour 设置打卡时间小时
 * calarm_minute 设置打卡时间分钟
 * work_hour 设置上班时长
 * clock_type 设置打卡方式
 * clock_hour 打卡的小时
 * clock_minute 打卡的分钟
 */

public class UniqueKeyAnn {
    public static final String CALARM_MONTH = "calarm_month";
    public static final String CALARM_HOUR = "calarm_hour";
    public static final String CALARM_MINUTE = "calarm_minute";
    public static final String WORK_HOUR = "work_hour";
    public static final String CLOCK_TYPE = "clock_type";
    public static final String CLOCK_HOUR = "clock_hour";
    public static final String CLOCK_MINUTE = "clock_minute";


    @StringDef({CALARM_MONTH,CALARM_HOUR,CALARM_MINUTE,WORK_HOUR,CLOCK_TYPE,CLOCK_HOUR,CLOCK_MINUTE})
    public @interface AllKey{}
}
