package com.clock.zc.punchtheclock.ui;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.clock.zc.punchtheclock.R;
import com.clock.zc.punchtheclock.base.BaseActivity;
import com.clock.zc.punchtheclock.util.AccountMgr;
import com.clock.zc.punchtheclock.util.Content;
import com.clock.zc.punchtheclock.util.UniqueKey;

//import org.w3c.dom.Text;
//import org.xutils.view.annotation.ContentView;
//import org.xutils.view.annotation.Event;
//import org.xutils.view.annotation.ViewInject;
//import org.xutils.x;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//@ContentView(R.layout.activity_setting)
public class SettingActivity extends BaseActivity {

    //选择时间Dialog
    private TimePickerDialog timePickerDialog;
    private Calendar calendar;
    @BindView(R.id.rl_set)
    RelativeLayout rl_set;
    @BindView(R.id.tv_work_time)
    TextView tv_work_time;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_work_hour)
    TextView tv_work_hour;
    @BindView(R.id.tv_clock_type)
    TextView tv_clock_type;
    @BindView(R.id.tv_version)
    TextView tv_version;



    private int mWorkHour = 8;
    private String sType = Content.WIFI_TYPE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        calendar = Calendar.getInstance();
        title.setText("设置");
        if(amr.getInt(UniqueKey.calarm_hour,-1)!=-1){
            String str = amr.getInt(UniqueKey.calarm_hour,-1)+":"+amr.getInt(UniqueKey.calarm_minute,-1);
            tv_work_time.setText(str);
        }
        if(amr.getInt(UniqueKey.work_hour,-1)!=-1){
            tv_work_hour.setText(amr.getInt(UniqueKey.work_hour,-1)+"小时");
            mWorkHour = amr.getInt(UniqueKey.work_hour,-1);
        }
        if(!TextUtils.isEmpty(amr.getString(UniqueKey.clock_type))){
            sType = amr.getString(UniqueKey.clock_type);
            tv_clock_type.setText(sType);
        }
        String version = getVersion();
        tv_version.setText(version);

    }
    @OnClick({R.id.back,R.id.rl_work_time,R.id.rl_work_hour,R.id.rl_clock_type,R.id.tv_version})
    void onEvent(View v){
        switch (v.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.rl_work_time:
                showTime();
                break;
            case R.id.rl_work_hour:
                showPopu(title);
                break;
            case R.id.rl_clock_type:
                showPopuType(title);
                break;
            case R.id.tv_version:
//                Intent intent = new Intent();
//                intent.setClass(context,HistoryActivity.class);
//                startActivity(intent);
                startAct(HistoryActivity.class);
                break;
            default:
                break;
        }
    }
    private void showTime() {
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Log.d("测试", Integer.toString(hourOfDay));
                Log.d("测试", Integer.toString(minute));
                amr.putInt(UniqueKey.calarm_hour,hourOfDay);
                amr.putInt(UniqueKey.calarm_minute,minute);
                String str = hourOfDay+":"+minute;
                tv_work_time.setText(str);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
        timePickerDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }
    private void showPopu(View view){
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentview = inflater.inflate(R.layout.pop_view, null);
        final PopupWindow popupWindow = new PopupWindow(contentview, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);

        NumberPicker numberPicker = (NumberPicker) contentview.findViewById(R.id.hourpicker);
        numberPicker.setMaxValue(23);
        numberPicker.setMinValue(0);
        numberPicker.setValue(mWorkHour);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mWorkHour = newVal;
            }
        });
        contentview.findViewById(R.id.tv_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amr.putInt(UniqueKey.work_hour,mWorkHour);
                tv_work_hour.setText(mWorkHour+"小时");
                popupWindow.dismiss();
            }
        });

        contentview.findViewById(R.id.tv_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(view,  Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
    }
    private void showPopuType(View view){
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentview = inflater.inflate(R.layout.pop_view, null);
        final PopupWindow popupWindow = new PopupWindow(contentview, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);

        NumberPicker numberPicker = (NumberPicker) contentview.findViewById(R.id.hourpicker);
        TextView tv_instruction = (TextView) contentview.findViewById(R.id.tv_instruction);
        tv_instruction.setVisibility(View.GONE);
        final String type[] = {Content.WIFI_TYPE,Content.SENSOR_TYPE};
        numberPicker.setDisplayedValues(type);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(type.length - 1);
        int index = 0;
        for (int i = 0; i < type.length; i++) {
            if(sType.equals(type[i])){
                index = i;
            }
        }
        numberPicker.setValue(index);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                sType = type[newVal];
            }
        });
        contentview.findViewById(R.id.tv_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amr.putString(UniqueKey.clock_type,sType);
                tv_clock_type.setText(sType);
                popupWindow.dismiss();
            }
        });
        contentview.findViewById(R.id.tv_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(view,  Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
    }
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
