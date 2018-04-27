package com.clock.zc.punchtheclock.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.pm.PackageManager.GET_ACTIVITIES;
import static android.content.pm.PackageManager.NameNotFoundException;
import static java.lang.Thread.UncaughtExceptionHandler;
import static java.lang.Thread.getDefaultUncaughtExceptionHandler;
import static java.lang.Thread.setDefaultUncaughtExceptionHandler;

/**
 * Created by Zc on 2017/11/2.
 */

public class MyExceptionHandler implements UncaughtExceptionHandler {
    // 上下文
    private Context mContext;
    // 是否打开上传
    public boolean openUpload = true;
    // Log文件路径
    private static final String LOG_FILE_DIR = "log";
    // log文件的后缀名
    private static final String FILE_NAME = ".log";
    private static MyExceptionHandler instance = null;
    // 系统默认的异常处理（默认情况下，系统会终止当前的异常程序）
    private UncaughtExceptionHandler mDefaultCrashHandler;

    private MyExceptionHandler(Context cxt) {
        // 获取系统默认的异常处理器
        mDefaultCrashHandler = getDefaultUncaughtExceptionHandler();
        // 将当前实例设为系统默认的异常处理器
        setDefaultUncaughtExceptionHandler(this);
        // 获取Context，方便内部使用
        this.mContext = cxt.getApplicationContext();
    }

    public synchronized static MyExceptionHandler create(Context cxt) {
        if (instance == null) {
            instance = new MyExceptionHandler(cxt);
        }
        return instance;
    }

    /**
     * 当程序中有未被捕获的异常，系统将会自动调用#uncaughtException方法
     * thread为出现未捕获异常的线程，ex为未捕获的异常，有了这个ex，我们就可以得到异常信息。
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        try {
            // 保存导出异常日志信息到SD卡中
            saveToSDCard(ex);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 如果系统提供了默认的异常处理器，则交给系统去结束我们的程序，否则就由我们自己结束自己
            Toast.makeText(mContext,
                    "很抱歉，程序出错，即将退出:\r\n" + ex.getLocalizedMessage(),
                    Toast.LENGTH_LONG).show();
            if (mDefaultCrashHandler != null) {
                mDefaultCrashHandler.uncaughtException(thread, ex);
            } else {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 保存文件到SD卡
     *
     */
    private void saveToSDCard(Throwable ex) throws Exception {
        //
        String path =  Environment.getExternalStorageDirectory()+File.separator +mContext.getPackageName()+ File.separator+ LOG_FILE_DIR;
        String name = getDataTime("yyyyMMddHHmmss") + FILE_NAME;
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
        File logfile = new File(path+File.separator+name);
        if(!logfile.exists()){
            logfile.createNewFile();
        }
        PrintWriter pw = new PrintWriter(new BufferedWriter(
                new FileWriter(logfile)));
        // 导出发生异常的时间
        pw.println(getDataTime("yyyy-MM-dd-HH-mm-ss"));
        // 导出手机信息
        savePhoneInfo(pw);
        pw.println();
        // 导出异常的调用栈信息
        ex.printStackTrace(pw);
        pw.close();
    }

    /**
     * 保存手机硬件信息
     *
     */
    private void savePhoneInfo(PrintWriter pw) throws NameNotFoundException {
        // 应用的版本名称和版本号
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(),
                GET_ACTIVITIES);
        pw.print("App Version: ");
        pw.print(pi.versionName);
        pw.print('_');
        pw.println(pi.versionCode);
        pw.println();

        // android版本号
        pw.print("OS Version: ");
        pw.print(Build.VERSION.RELEASE);
        pw.print("_");
        pw.println(Build.VERSION.SDK_INT);
        pw.println();

        // 手机制造商
        pw.print("Manufacturer: ");
        pw.println(Build.MANUFACTURER);
        pw.println();

        // 手机型号
        pw.print("Model: ");
        pw.println(Build.MODEL);
        pw.println();
    }

    /**
     * 根据时间格式返回时间
     *
     */
    private String getDataTime(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date());
    }
}
