package com.clock.zc.punchtheclock.ui;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clock.zc.punchtheclock.R;
import com.clock.zc.punchtheclock.base.BaseActivity;
//import com.clock.zc.punchtheclock.bean.ClockHistory;
import com.clock.zc.punchtheclock.bean.ClockBean;
import com.clock.zc.punchtheclock.reciver.AlarmReceiver;
import com.clock.zc.punchtheclock.util.Content;
import com.clock.zc.punchtheclock.util.EffectsDialogUtil;
import com.clock.zc.punchtheclock.util.TimeUtil;
import com.clock.zc.punchtheclock.util.UniqueKeyAnn;
import com.clock.zc.punchtheclock.view.Explosion.ExplosionField;
import com.clock.zc.punchtheclock.view.ShakeAnimator;
import com.clock.zc.punchtheclock.view.StatusBarCompat;
import com.clock.zc.punchtheclock.view.TransitionHelper;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.support.v4.widget.DrawerLayout.STATE_DRAGGING;
import static android.support.v4.widget.DrawerLayout.STATE_SETTLING;

//@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{
    @BindView(R.id.head)
    RelativeLayout head;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.btn_title_right)
    TextView btn_title_right;
    @BindView(R.id.tv_week)
    TextView tv_week;
    @BindView(R.id.tv_hour)
    TextView tv_hour;
    @BindView(R.id.tv_date)
    TextView tv_date;
    @BindView(R.id.to_sensor)
    TextView to_sensor;
    @BindView(R.id.tv_off)
    TextView tv_off;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    View headerLayout;

    String lastDate="";//上次打卡时间
    String oldTime;
    private String newTime;
    private String sType;
    private long lastClickTime;
    AlarmManager am;
    MyHandler myHandler = null;
    protected ExplosionField explosionField;
    ObjectAnimator animator;
    private EffectsDialogUtil effectsDialogUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_drawer_layout);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
//                Window window = getWindow(); View decorView = window.getDecorView();
//                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
//                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//                decorView.setSystemUiVisibility(option);
//                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                window.setStatusBarColor(Color.TRANSPARENT); //导航栏颜色也可以正常设置 //
//                window.setNavigationBarColor(Color.TRANSPARENT);
//            } else {
//                Window window = getWindow();
//                WindowManager.LayoutParams attributes = window.getAttributes();
//                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
//                attributes.flags |= flagTranslucentStatus; // attributes.flags |= flagTranslucentNavigation;
//                window.setAttributes(attributes);
//            }
//        }


        ButterKnife.bind(this);
        explosionField = new ExplosionField(this);

        //ActionBarDrawerToggle作用是在toolbar上创建一个点击弹出drawer的按钮而已
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View view, float v) {}
            @Override
            public void onDrawerOpened(View view) {}
            @Override
            public void onDrawerClosed(View view) {}
            @Override
            public void onDrawerStateChanged(int i) {
                if(i == STATE_DRAGGING){
                    StatusBarCompat.compat(MainActivity.this, Color.TRANSPARENT);
                }else if(i == STATE_SETTLING){
                    StatusBarCompat.compat(MainActivity.this, getResources().getColor(R.color.background));
                }else{
                    if(drawer.isDrawerOpen(GravityCompat.START)){
                        StatusBarCompat.compat(MainActivity.this, Color.TRANSPARENT);
                    }else{
                        StatusBarCompat.compat(MainActivity.this, getResources().getColor(R.color.background));
                    }
                }
            }
        });
        //不写这句话，是没有按钮显示的
//        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);
        headerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(context, PersonCenterActivity.class);
                Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants(MainActivity.this, false,
                        new Pair<>(headerLayout, "share_layout"));
                ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, pairs);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    startActivity(i, transitionActivityOptions.toBundle());
                }else{
                    startActivity(i);
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        title.setText("每日打卡");
//        back.setVisibility(View.GONE);
        btn_title_right.setBackgroundResource(R.mipmap.setting);
        setViewWH(btn_title_right);
        tv_week.setText(TimeUtil.getWeekString());
        tv_hour.setText(TimeUtil.getDataHour());
        tv_date.setText(TimeUtil.StringData());
        lastDate = amr.getVal(UniqueKeyAnn.CALARM_MONTH);

        oldTime = TimeUtil.getYMD(lastDate);
        newTime = TimeUtil.getYMD(TimeUtil.getData());
        if(!TextUtils.isEmpty(lastDate)&&oldTime.equals(newTime)){
            time.setText("今日打卡时间:"+TimeUtil.getHM(lastDate));
            setOffView();
        }
        sType = amr.getString(UniqueKeyAnn.CLOCK_TYPE);
        if(!TextUtils.isEmpty(sType)){
            to_sensor.setVisibility(View.VISIBLE);
            to_sensor.setText(sType+"打卡");
            if(sType.equals(Content.WIFI_TYPE)){
                if(!oldTime.equals(newTime)){
                    clockByWifi();
                }else{
                    toast("您已经打卡了");
                }
            }
        }else{
            to_sensor.setVisibility(View.GONE);
        }
        myHandler = new MyHandler(MainActivity.this,effectsDialogUtil);
//        explosionField.addListener(dialogBuilder.getCurrentFocus());
//        head.setBackgroundColor(Color.parseColor("#0000ff"));
        if(drawer.isDrawerOpen(GravityCompat.START)){
            StatusBarCompat.compat(this, Color.TRANSPARENT);
        }else{
            StatusBarCompat.compat(this, getResources().getColor(R.color.background));
        }
        boolean flag = amr.getInt(UniqueKeyAnn.CALARM_HOUR,-1)==-1 && TextUtils.isEmpty(amr.getVal(UniqueKeyAnn.CALARM_MONTH));
        if(flag){
            animator = ShakeAnimator.nope(time);
            animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.start();
        }
    }
    private void setOffView(){
        tv_off.setVisibility(View.VISIBLE);
        if(animator!=null && animator.isStarted()){
            animator.cancel();
        }
        if(amr.getInt(UniqueKeyAnn.WORK_HOUR,-1)!=-1){
            int off_hour = amr.getInt(UniqueKeyAnn.CLOCK_HOUR,-1)+amr.getInt(UniqueKeyAnn.WORK_HOUR,-1);
            String str = "";
            if(off_hour>=24){//第二天下班
                int diff = off_hour-24;
                if(diff>=10){
                    str = "下班时间：明天"+diff+":"+amr.getInt(UniqueKeyAnn.CLOCK_MINUTE,-1);
                }else{
                    str = "下班时间：明天0"+diff+":"+amr.getInt(UniqueKeyAnn.CLOCK_MINUTE,-1);
                }

                tv_off.setText(str);
                startRemind(diff);
            }else if(off_hour<24 && off_hour>0){//今天下班
                tv_off.setText("下班时间：今天"+ off_hour+":"+amr.getInt(UniqueKeyAnn.CLOCK_MINUTE,-1));
                startRemind(off_hour);
            }else{
                tv_off.setVisibility(View.GONE);
            }
        }else{
            tv_off.setVisibility(View.GONE);
            toast("您没有设置上班时长");
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
//        String wifi = "YTO-YH1";
//        String wifi1= "YTO-YH";
//        time.setText(wifiName);
        List<Integer> sList = TimeUtil.getDateListInt();
        int hour = sList.get(3);
        int minute = sList.get(4);
        if (amr.getInt(UniqueKeyAnn.CALARM_HOUR, -1) != -1) {
            if(wifiName.contains("YTO-YH")){
                if (hour < amr.getInt(UniqueKeyAnn.CALARM_HOUR, -1)) {
                    setDB(hour,minute,1);
                } else if (hour == amr.getInt(UniqueKeyAnn.CALARM_HOUR, -1)) {
                    if (minute < amr.getInt(UniqueKeyAnn.CALARM_MINUTE, -1)) {
                        setDB(hour,minute,1);
                    } else {
                        toastCenter("很抱歉，你迟到了，明天加油哦");
                    }
                } else {
                    toastCenter("很抱歉，你迟到了，明天加油哦");
                }
            } else {
                toastCenter("不是工作WiFi，请切换");
            }
        } else {
            setDialog("提示", "请设置上班时间","确定","暂不设置");
//            dialogUtil.inform(context, "提示", "请设置上班时间","确定", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    Intent intent = new Intent();
//                    intent.setClass(context,SettingActivity.class);
//                    startActivity(intent);
//                }
//            });
//            toastCenter("请设置上班时间");

        }
    }

    @OnClick({R.id.back,R.id.btn_title_right,R.id.time,R.id.to_sensor})
    void toggleEvent(View v){
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_title_right:

//                intent.setClass(this,SettingActivity.class);
//                startActivity(intent);
                startAct(SettingActivity.class);

                break;
            case R.id.time:

                setClockTime(0);
                break;
            case R.id.to_sensor:
                if(!TextUtils.isEmpty(sType)&&sType.equals(Content.SENSOR_TYPE)){
                    intent.setClass(this,SensorActivity.class);
                    startActivityForResult(intent,101);
                }
                break;
            case R.id.back:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                    StatusBarCompat.compat(this, getResources().getColor(R.color.background));
                } else {
                    drawer.openDrawer(GravityCompat.START);
                    StatusBarCompat.compat(this, Color.TRANSPARENT);
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
            setClockTime(2);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(animator!=null && animator.isStarted()){
            animator.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        stopRemind();
        myHandler.removeMessages(0);
        myHandler.removeMessages(1);
    }
    private void setDB(int hour,int minute,int type){
        amr.putString(UniqueKeyAnn.CALARM_MONTH, TimeUtil.getData());
        amr.putInt(UniqueKeyAnn.CLOCK_HOUR,hour);
        amr.putInt(UniqueKeyAnn.CLOCK_MINUTE,minute);
        ClockBean clockBean = new ClockBean();
        clockBean.setTime(System.currentTimeMillis());
        clockBean.setStatue(1);
        clockBean.setType(type);
        liteOrm.insert(clockBean);
        time.setText("今日打卡时间:" + TimeUtil.getDataHour());
        setOffView();

    }
    private void setClockTime(int type){
        long start = System.currentTimeMillis();
        lastDate = amr.getVal(UniqueKeyAnn.CALARM_MONTH);
        oldTime = TimeUtil.getYMD(lastDate);
        newTime = TimeUtil.getYMD(TimeUtil.getData());
        if (amr.getInt(UniqueKeyAnn.CALARM_HOUR, -1) != -1) {
            if (!oldTime.equals(newTime)) {
                List<Integer> sList = TimeUtil.getDateListInt();
                int hour = sList.get(3);
                int minute = sList.get(4);
                if (hour < amr.getInt(UniqueKeyAnn.CALARM_HOUR, -1)) {
                    explosionField.explode(time);
                    setDB(hour,minute,type);
                } else if (hour == amr.getInt(UniqueKeyAnn.CALARM_HOUR, -1)) {
                    if (minute < amr.getInt(UniqueKeyAnn.CALARM_MINUTE, -1)) {
                        explosionField.explode(time);
                        setDB(hour,minute,type);
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
            setDialog("提示", "请设置上班时间","确定","暂不设置");
//            dialogUtil.inform(context, "提示", "请设置上班时间","确定", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    Intent intent = new Intent();
//                    intent.setClass(context,SettingActivity.class);
//                    startActivity(intent);
//                }
//            });
//                    toastCenter("请设置上班时间");
        }

    }
    /*
    * Effectstype.Fadein
    * Effectstype.Slideright 从右平移进入
    * Effectstype.Slideleft 从左平移进入
    * Effectstype.Slidetop从上平移进入
    * Effectstype.SlideBottom从下平移进入
    * Effectstype.Newspager 旋转进入
    * Effectstype.Fall
    * Effectstype.Sidefill
    * Effectstype.Fliph
    * Effectstype.Flipv
    * Effectstype.RotateBottom
    * Effectstype.RotateLeft
    * Effectstype.Slit
    * Effectstype.Shake
    * */
    private void setDialog(String title,String msg,String sure,String cancle){//
//        dialogBuilder
//                .withTitle(title)                                  //.withTitle(null)  no title
//                .withTitleColor("#FFFFFF")                                  //def
//                .withDividerColor("#11000000")                              //def
//                .withMessage(msg)                     //.withMessage(null)  no Msg
//                .withMessageColor("#FFFFFFFF")                              //def  | withMessageColor(int resid)
//                .withDialogColor("#00aeFF")                               //def  | withDialogColor(int resid)                               //def
////                .withIcon(getResources().getDrawable(R.drawable.icon))
//                .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
//                .withDuration(700)                                          //def
//                .withEffect(Effectstype.Newspager)                                         //def Effectstype.Slidetop
//                .withButton1Text(sure)                                      //def gone
//                .withButton2Text(cancle)                                  //def gone
////                .setCustomView(R.layout.custom_view,v.getContext())         //.setCustomView(View or ResId,context)
//                .setButton1Click(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                        Toast.makeText(v.getContext(), "i'm btn1", Toast.LENGTH_SHORT).show();
//                        explosionField.explode(v.getRootView(),myHandler);
////                        dialogBuilder.dismiss();
//                    }
//                })
//                .isCancelableOnTouchOutside(false)
//                .setButton2Click(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                        Toast.makeText(v.getContext(), "i'm btn2", Toast.LENGTH_SHORT).show();
////                        explosionField.explode(v.getRootView(),myHandler);
//                        dialogBuilder.dismiss();
//                    }
//                })
//                .show();
        effectsDialogUtil.createDoubleDialog(context, title, Effectstype.Newspager, msg, sure, cancle, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                explosionField.explode(v.getRootView(),myHandler);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                effectsDialogUtil.cancleDialog();
            }
        });

    }
    /**
     * 开启提醒
     */
    private void startRemind(int hour){

        //得到日历实例，主要是为了下面的获取时间
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(System.currentTimeMillis());

        //获取当前毫秒值
        long systemTime = System.currentTimeMillis();

        //是设置日历的时间，主要是让日历的年月日和当前同步
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        // 这里时区需要设置一下，不然可能个别手机会有8个小时的时间差
        mCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        //设置在几点提醒  设置的为13点
        mCalendar.set(Calendar.HOUR_OF_DAY, hour);
        //设置在几分提醒  设置的为25分
        mCalendar.set(Calendar.MINUTE, amr.getInt(UniqueKeyAnn.CLOCK_MINUTE,-1));
        //下面这两个看字面意思也知道
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);

        //上面设置的就是13点25分的时间点

        //获取上面设置的13点25分的毫秒值
        long selectTime = mCalendar.getTimeInMillis();

        // 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
        if(systemTime > selectTime) {
            mCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        //AlarmReceiver.class为广播接受者
        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
        intent.putExtra("msg","你可以打酱油了");
        PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
        //得到AlarmManager实例
        am = (AlarmManager)getSystemService(ALARM_SERVICE);

        //**********注意！！下面的两个根据实际需求任选其一即可*********

        /**
         * 单次提醒
         * mCalendar.getTimeInMillis() 上面设置的13点25分的时间点毫秒值
         */
        am.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pi);

        /**
         * 重复提醒
         * 第一个参数是警报类型；
         * 第二个参数网上说法不一，很多都是说的是延迟多少毫秒执行这个闹钟，但是我用的刷了MIUI的三星手机的实际效果是与单次提醒的参数一样，即设置的13点25分的时间点毫秒值
         * 第三个参数是重复周期，也就是下次提醒的间隔 毫秒值 我这里是一天后提醒
         */
//        am.setRepeating(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), 1000, pi);//1000 * 60 * 60 * 24
//        Toast.makeText(this, "开启了提醒", Toast.LENGTH_SHORT).show();
    }


    /**
     * 关闭提醒
     */
    private void stopRemind(){

        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, 0,
                intent, 0);
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        //取消警报
        am.cancel(pi);
        Toast.makeText(this, "关闭了提醒", Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.nav_camera) {
            // 点击侧边栏第一项打开新的activity，看不同主题的影响
//            Intent intent = new Intent(MainActivity.this,SettingActivity.class);
//            startActivity(intent);
            startAct(SettingActivity.class);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class MyHandler extends Handler {
        private final WeakReference<MainActivity> mActivity;

        private final WeakReference<EffectsDialogUtil> diaBul;

        public MyHandler(MainActivity activity,EffectsDialogUtil dialog) {
            mActivity = new WeakReference<MainActivity>(activity);
            diaBul = new WeakReference<EffectsDialogUtil>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            System.out.println(msg);
            final Activity activity=mActivity.get();
            final EffectsDialogUtil dialog = diaBul.get();
            if (mActivity.get() == null) {
                return;
            }
            if(msg.what==0){
//                    Intent intent = new Intent();
//                    intent.setClass(activity,SettingActivity.class);
//                    activity.startActivity(intent);
                dialog.cancleDialog();
                startAct(SettingActivity.class);
            }
        }
    }
}
