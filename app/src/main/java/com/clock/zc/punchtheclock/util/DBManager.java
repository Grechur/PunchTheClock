package com.clock.zc.punchtheclock.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.clock.zc.punchtheclock.bean.ClockHistory;
import com.clock.zc.punchtheclock.bean.ClockHistoryDao;
import com.clock.zc.punchtheclock.bean.DaoMaster;
import com.clock.zc.punchtheclock.bean.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by Zc on 2017/8/30.
 */

public class DBManager {
    private final static String dbName = "clock_db";
    private static DBManager mInstance;
    private DaoMaster.DevOpenHelper openHelper;
    private Context context;

    public DBManager(Context context) {
        this.context = context;
        openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
    }

    /**
     * 获取单例引用
     *
     * @param context
     * @return
     */
    public static DBManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (DBManager.class) {
                if (mInstance == null) {
                    mInstance = new DBManager(context);
                }
            }
        }
        return mInstance;
    }
    /**
     * 获取可读数据库
     */
    private SQLiteDatabase getReadableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db;
    }
    /**
     * 获取可写数据库
     */
    private SQLiteDatabase getWritableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db;
    }

    /**
     * 插入一条记录
     *
     * @param user
     */
    public void insertClockHistory(ClockHistory user) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        ClockHistoryDao userDao = daoSession.getClockHistoryDao();
        userDao.insert(user);
    }

    /**
     * 插入用户集合
     *
     * @param users
     */
    public void insertClockHistoryList(List<ClockHistory> users) {
        if (users == null || users.isEmpty()) {
            return;
        }
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        ClockHistoryDao userDao = daoSession.getClockHistoryDao();
        userDao.insertInTx(users);
    }
    /**
     * 删除一条记录
     *
     * @param user
     */
    public void deleteClockHistory(ClockHistory user) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        ClockHistoryDao userDao = daoSession.getClockHistoryDao();
        userDao.delete(user);
    }
    /**
     * 更新一条记录
     *
     * @param user
     */
    public void updateClockHistory(ClockHistory user) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        ClockHistoryDao userDao = daoSession.getClockHistoryDao();
        userDao.update(user);
    }

    /**
     * 查询用户列表
     */
    public List<ClockHistory> queryClockHistoryList() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        ClockHistoryDao userDao = daoSession.getClockHistoryDao();
        QueryBuilder<ClockHistory> qb = userDao.queryBuilder();
        List<ClockHistory> list = qb.list();
        return list;
    }

    /**
     * 查询用户列表
     */
    public List<ClockHistory> queryClockHistoryList(int statue) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        ClockHistoryDao userDao = daoSession.getClockHistoryDao();
        QueryBuilder<ClockHistory> qb = userDao.queryBuilder();
        qb.where(ClockHistoryDao.Properties.Statue.gt(statue)).orderAsc(ClockHistoryDao.Properties.Statue);
        List<ClockHistory> list = qb.list();
        return list;
    }
}
