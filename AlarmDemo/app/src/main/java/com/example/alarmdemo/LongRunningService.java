package com.example.alarmdemo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.util.Log;

public class LongRunningService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        doSomething();

        long triggerAtMillis = System.currentTimeMillis() + 1000;   // 每1秒
        Intent i = new Intent(this, LongRunningService.class);  // 重复自己（以实现循环任务）
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pi);

        return super.onStartCommand(intent, flags, startId);
    }

    private void doSomething() {
        LogUtil.log("do something...");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
