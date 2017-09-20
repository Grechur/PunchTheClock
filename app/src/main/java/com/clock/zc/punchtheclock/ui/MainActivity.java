package com.clock.zc.punchtheclock.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clock.zc.punchtheclock.R;
import com.clock.zc.punchtheclock.base.BaseActivity;
//import com.clock.zc.punchtheclock.bean.ClockHistory;
import com.clock.zc.punchtheclock.bean.ClockBean;
import com.clock.zc.punchtheclock.util.Content;
import com.clock.zc.punchtheclock.util.TimeUtil;
import com.clock.zc.punchtheclock.util.UniqueKey;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;
@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @ViewInject(R.id.time)
    private TextView time;
    @ViewInject(R.id.back)
    private ImageView back;
    @ViewInject(R.id.title)
    private TextView title;
    @ViewInject(R.id.btn_title_right)
    private TextView btn_title_right;
    @ViewInject(R.id.tv_week)
    private TextView tv_week;
    @ViewInject(R.id.tv_hour)
    private TextView tv_hour;
    @ViewInject(R.id.tv_date)
    private TextView tv_date;
    @ViewInject(R.id.to_sensor)
    private TextView to_sensor;

    String lastDate="";//上次打卡时间
    String oldTime;
    private String newTime;
    private String sType;
    private long lastClickTime;
    @Override
    protected void onResume() {
        super.onResume();
        title.setText("每日打卡");
        back.setVisibility(View.GONE);
        btn_title_right.setBackgroundResource(R.mipmap.setting);
        setViewWH(btn_title_right);
        tv_week.setText(TimeUtil.getWeekString());
        tv_hour.setText(TimeUtil.getDataHour());
        tv_date.setText(TimeUtil.StringData());
        lastDate = amr.getVal(UniqueKey.calarm_math);

        oldTime = TimeUtil.getYMD(lastDate);
        newTime = TimeUtil.getYMD(TimeUtil.getData());
        if(!TextUtils.isEmpty(lastDate)&&oldTime.equals(newTime)){
            time.setText("今日打卡时间:"+TimeUtil.getHM(lastDate));
        }
        sType = amr.getString(UniqueKey.clock_type);
        if(!TextUtils.isEmpty(sType)){
            to_sensor.setVisibility(View.VISIBLE);
            to_sensor.setText(sType+"打卡");
            if(sType.equals(Content.WIFI_TYPE)&&!oldTime.equals(newTime)){
                clockByWifi();
            }else{
                toast("您已经打卡了");
            }
        }else{
            to_sensor.setVisibility(View.GONE);
        }

    }

    private void setViewWH(View view){
        RelativeLayout.LayoutParams params =  (RelativeLayout.LayoutParams)view.getLayoutParams();
        params.height = dip2px(context,25);
        params.width = dip2px(context,25);
        view.setLayoutParams(params);
    }
    private String getConnectWifiSsid(){
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        Log.d("wifiInfo", wifiInfo.toString());
        Log.d("SSID",wifiInfo.getSSID());
        return wifiInfo.getSSID().replace("\"","");
    }

    private void clockByWifi(){
        String wifiName = getConnectWifiSsid();
        String wifi = "YTO-YH1";
        String wifi1= "YTO-YH";
//        time.setText(wifiName);
        List<Integer> sList = TimeUtil.getDateListInt();
        int hour = sList.get(3);
        int minute = sList.get(4);
        if (amr.getInt(UniqueKey.calarm_hour, -1) != -1) {
            if (hour < amr.getInt(UniqueKey.calarm_hour, -1) && (wifiName.equals(wifi) || wifiName.equals(wifi1))) {
                amr.putString(UniqueKey.calarm_math, TimeUtil.getData());
//                ClockHistory clockHistory = new ClockHistory();
//                clockHistory.setTime(TimeUtil.getData());
//                clockHistory.setStatue(1);
//                insertDB(clockHistory);
                ClockBean clockBean = new ClockBean();
                clockBean.setTime(TimeUtil.getData());
                clockBean.setStatue(1);
                liteOrm.insert(clockBean);
                time.setText("今日打卡时间:" + TimeUtil.getDataHour());
            } else if (hour == amr.getInt(UniqueKey.calarm_hour, -1)) {
                if (minute < amr.getInt(UniqueKey.calarm_minute, -1)) {
                    time.setText("今日打卡时间:" + TimeUtil.getDataHour());
                } else {
                    toastCenter("很抱歉，你迟到了，明天加油哦");
                }
            } else {
                toastCenter("很抱歉，你迟到了，明天加油哦");
            }
        } else {
            dialogUtil.inform(context, "提示", "请设置上班时间","确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.setClass(context,SettingActivity.class);
                    startActivity(intent);
                }
            });
//            toastCenter("请设置上班时间");
        }
    }

    @Event({R.id.back,R.id.btn_title_right,R.id.time,R.id.to_sensor})
    private void toggleEvent(View v){
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_title_right:
                intent.setClass(this,SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.time:
                long start = System.currentTimeMillis();
                lastDate = amr.getVal(UniqueKey.calarm_math);
                oldTime = TimeUtil.getYMD(lastDate);
                newTime = TimeUtil.getYMD(TimeUtil.getData());
                if (amr.getInt(UniqueKey.calarm_hour, -1) != -1) {
                    if (!oldTime.equals(newTime)) {
                        List<Integer> sList = TimeUtil.getDateListInt();
                        int hour = sList.get(3);
                        int minute = sList.get(4);
                        if (hour < amr.getInt(UniqueKey.calarm_hour, -1)) {
                            amr.putString(UniqueKey.calarm_math, TimeUtil.getData());
//                            ClockHistory clockHistory = new ClockHistory();
//                            clockHistory.setTime(TimeUtil.getData());
//                            clockHistory.setStatue(1);
//                            ClockHistory clockHistory1 = new ClockHistory();
//                            insertDB(clockHistory);
                            ClockBean clockBean = new ClockBean();
                            clockBean.setTime(TimeUtil.getData());
                            clockBean.setStatue(1);
                            liteOrm.insert(clockBean);
                            time.setText("今日打卡时间:" + TimeUtil.getDataHour());
                        } else if (hour == amr.getInt(UniqueKey.calarm_hour, -1)) {
                            if (minute < amr.getInt(UniqueKey.calarm_minute, -1)) {
                                time.setText("今日打卡时间:" + TimeUtil.getDataHour());
                            } else {
                                toastCenter("很抱歉，你迟到了，明天加油哦");
                            }
                        } else {
                            toastCenter("很抱歉，你迟到了，明天加油哦");
                        }
                    } else {
                        long time = start-lastClickTime;
                        if(time>=1000&&time<2000){
                            toast("您已经打卡了,不要再打了");
                        }else if(time>=500&&time<1000){
                            toast("您已经打卡了,不要再打了,我要生气了");
                        }else if(time>=200&&time<500){
                            toast("手速这么快，单身多久了？");
                        }else if(time<200){
                            toast("请珍惜手机以及女票");
                        }else{
                            toast("您已经打卡了");
                        }
                        lastClickTime = start;
                    }
                }else{
                    dialogUtil.inform(context, "提示", "请设置上班时间","确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.setClass(context,SettingActivity.class);
                            startActivity(intent);
                        }
                    });
//                    toastCenter("请设置上班时间");
                }
                break;
            case R.id.to_sensor:
                if(!TextUtils.isEmpty(sType)&&sType.equals(Content.SENSOR_TYPE)){
                    intent.setClass(this,SensorActivity.class);
                    startActivityForResult(intent,101);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101&&resultCode==102) {
            lastDate = amr.getVal(UniqueKey.calarm_math);
            oldTime = TimeUtil.getYMD(lastDate);
            newTime = TimeUtil.getYMD(TimeUtil.getData());
            if (amr.getInt(UniqueKey.calarm_hour, -1) != -1) {
                if (!oldTime.equals(newTime)) {
                    List<Integer> sList = TimeUtil.getDateListInt();
                    int hour = sList.get(3);
                    int minute = sList.get(4);
                    if (hour < amr.getInt(UniqueKey.calarm_hour, -1)) {
                        amr.putString(UniqueKey.calarm_math, TimeUtil.getData());
//                        ClockHistory clockHistory = new ClockHistory();
//                        clockHistory.setTime(TimeUtil.getData());
//                        clockHistory.setStatue(1);
//                        insertDB(clockHistory);
                        ClockBean clockBean = new ClockBean();
                        clockBean.setTime(TimeUtil.getData());
                        clockBean.setStatue(1);
                        liteOrm.insert(clockBean);
                        time.setText("今日打卡时间:" + TimeUtil.getDataHour());
                    } else if (hour == amr.getInt(UniqueKey.calarm_hour, -1)) {
                        if (minute < amr.getInt(UniqueKey.calarm_minute, -1)) {
                            time.setText("今日打卡时间:" + TimeUtil.getDataHour());
                        } else {
                            toastCenter("很抱歉，你迟到了，明天加油哦");
                        }
                    } else {
                        toastCenter("很抱歉，你迟到了，明天加油哦");
                    }
                } else {
                    toast("您已经打卡了");
                }
            }else{
                dialogUtil.inform(context, "提示", "请设置上班时间","确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setClass(context,SettingActivity.class);
                        startActivity(intent);
                    }
                });
//                toastCenter("请设置上班时间");
            }
        }
    }


}
