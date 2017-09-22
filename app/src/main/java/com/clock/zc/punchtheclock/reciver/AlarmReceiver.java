package com.clock.zc.punchtheclock.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Zc on 2017/9/20.
 */

public class AlarmReceiver extends BroadcastReceiver {
    Vibrator vibrator;
    @Override
    public void onReceive(Context context, Intent intent) {

        //当系统到我们设定的时间点的时候会发送广播，执行这里
        Log.d("MyTag", "onclock......................");
        String msg = intent.getStringExtra("msg");
        Toast.makeText(context,msg, Toast.LENGTH_SHORT).show();
        vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
        long [] pattern = {100,400,100,400};   // 停止 开启 停止 开启
        vibrator.vibrate(pattern,-1);           //重复两次上面的pattern 如果只想震动一次，index设为-1
    }

}