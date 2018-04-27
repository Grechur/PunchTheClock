package com.clock.zc.punchtheclock.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.clock.zc.punchtheclock.util.MyExceptionHandler;

//import com.clock.zc.punchtheclock.util.DBManager;
//
//import org.greenrobot.greendao.AbstractDaoMaster;
//import org.xutils.BuildConfig;
//import org.xutils.x;

/**
 * Created by Zc on 2017/8/28.
 */

public class ClockApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
//        x.Ext.init(this);
//        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
        MyExceptionHandler.create(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
