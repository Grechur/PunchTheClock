package com.clock.zc.punchtheclock.base;

import android.app.Application;

//import com.clock.zc.punchtheclock.util.DBManager;
//
//import org.greenrobot.greendao.AbstractDaoMaster;
import org.xutils.BuildConfig;
import org.xutils.x;

/**
 * Created by Zc on 2017/8/28.
 */

public class ClockApplication extends Application{
//    private DBManager dbManager;
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
//        dbManager = DBManager.getInstance(getApplicationContext());
    }

//    public DBManager getDbManager() {
//        return dbManager;
//    }

}
