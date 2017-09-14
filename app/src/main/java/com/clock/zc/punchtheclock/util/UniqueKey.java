package com.clock.zc.punchtheclock.util;

/*
 * 所有存在同一个SharedPreference里的值都需要一个唯一的key，
 * 为了保证这些key的唯一性，我们借用就了enum的特性，
 * 用的时候将enum转成String使用即可
 */
public enum UniqueKey {

    calarm_math,calarm_hour,calarm_minute,work_hour,clock_type,

}
