package com.clock.zc.punchtheclock.util;

/**
 * Created by Zc on 2017/8/31.
 */

public class Content {
    public static final String WIFI_TYPE = "WIFI";
    public static final String SENSOR_TYPE = "摇一摇";
    public static final int ELAPSED_TIME =10* 1000;
    public static final int RETRIVE_SERVICE_COUNT = 50;
    public static final int ELAPSED_TIME_DELAY = 2*60*1000;//get GPS delayed
    public static final int BROADCAST_ELAPSED_TIME_DELAY = 2*60*1000;
    public static final String WORKER_SERVICE = "com.coder80.timer.service.WorkService";
    public static final String POI_SERVICE = "com.coder80.timer.service.UploadPOIService";
    public static final String POI_SERVICE_ACTION = "com.coder80.timer.service.UploadPOIService.action";
}
