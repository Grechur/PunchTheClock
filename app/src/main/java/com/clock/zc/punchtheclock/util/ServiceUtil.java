package com.clock.zc.punchtheclock.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.clock.zc.punchtheclock.service.UploadPOIService;

import java.util.List;

/**
 * Created by coder80 on 2014/10/31.
 */

public class ServiceUtil {

    public static boolean isServiceRunning(Context context, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceInfos = activityManager.getRunningServices(Content.RETRIVE_SERVICE_COUNT);

        if(null == serviceInfos || serviceInfos.size() < 1) {
            return false;
        }

        for(int i = 0; i < serviceInfos.size(); i++) {
            if(serviceInfos.get(i).service.getClassName().contains(className)) {
                isRunning = true;
                break;
            }
        }
        Log.i("ServiceUtilAlarmManager", className + " isRunning =  " + isRunning);
        return isRunning;
    }

    public static void invokeTimerPOIService(Context context){
        Log.i("ServiceUtilAlarmManager", "invokeTimerPOIService wac called.." );
        PendingIntent alarmSender = null;
        Intent startIntent = new Intent(context, UploadPOIService.class);
        startIntent.setAction(Content.POI_SERVICE_ACTION);
        try {
            alarmSender = PendingIntent.getService(context, 0, startIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        } catch (Exception e) {
            Log.i("ServiceUtilAlarmManager", "failed to start " + e.toString());
        }
        AlarmManager am = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), Content.ELAPSED_TIME, alarmSender);
    }

    public static void cancleAlarmManager(Context context){
        Log.i("ServiceUtilAlarmManager", "cancleAlarmManager to start ");
        Intent intent = new Intent(context,UploadPOIService.class);
    	intent.setAction(Content.POI_SERVICE_ACTION);
        PendingIntent pendingIntent=PendingIntent.getService(context, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm=(AlarmManager)context.getSystemService(Activity.ALARM_SERVICE);
        alarm.cancel(pendingIntent);
    }
}
