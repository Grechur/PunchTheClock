package com.clock.zc.punchtheclock.base;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clock.zc.punchtheclock.R;
import com.clock.zc.punchtheclock.bean.ClockHistory;
import com.clock.zc.punchtheclock.util.AccountMgr;
import com.clock.zc.punchtheclock.util.DBManager;
import com.clock.zc.punchtheclock.util.DialogUtil;

import org.xutils.x;

import java.util.Arrays;
import java.util.List;

public class BaseActivity extends AppCompatActivity {
    protected ClockApplication application;
    protected Context context;
    protected AccountMgr amr;
    protected DialogUtil dialogUtil;
    protected DBManager dbManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        context = this;
        application = (ClockApplication) getApplication();
        amr = new AccountMgr(context);
        dialogUtil = new DialogUtil();
        dbManager = application.getDbManager();
    }
    protected void insertDB(ClockHistory clockHistory){
        dbManager.insertClockHistory(clockHistory);
    }
    protected int dip2px(Context context,float dipValue)
    {
        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dipValue, r.getDisplayMetrics());
    }
    protected void toastCenter(String text) {
        try {
            Toast t = Toast.makeText(this, text,
                    Toast.LENGTH_SHORT);
            t.setGravity(Gravity.CENTER, 0, 0);
//            LinearLayout toastView = (LinearLayout) t.getView();
//            toastView.setBackgroundResource(R.drawable.blue_shape);
            View v = View.inflate(context,R.layout.toast_layout,null);
            TextView textView = (TextView) v.findViewById(R.id.tv_message);
            textView.setText(text);
            t.setView(v);
            t.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    protected void toast(String text) {
        try {
            Toast t = Toast.makeText(this, text,
                    Toast.LENGTH_SHORT);

//            LinearLayout toastView = (LinearLayout) t.getView();

//            toastView.setBackgroundResource(R.drawable.blue_shape);
            View v = View.inflate(context,R.layout.toast_layout,null);
            TextView textView = (TextView) v.findViewById(R.id.tv_message);
            textView.setText(text);
            t.setView(v);
            t.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
