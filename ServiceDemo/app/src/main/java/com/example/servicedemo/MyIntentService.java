package com.example.servicedemo;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by sjk on 17-6-7.
 */

public class MyIntentService extends IntentService {

    public MyIntentService() {
        super("MyIntentService");
        Logger.log("MyIntentService(), context: " + this);
    }

    public MyIntentService(String name) {
        super(name);
        Logger.log("MyIntentService(String)");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Logger.log("MyIntentService onHandleIntent。线程id为：" + Thread.currentThread().getId());

        // 模拟一个耗时操作
        for (int i = 0; i < 5; ++i) {
            Logger.log("Progress: " + (i + 1) * 20 + "%");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Logger.log(e.getMessage());
            }
        }
    }

    /*因为是IntentService，所以该方法会自动回调，在任务完成之后
    不需要在外部调用stopService或内部调用stopSelf方法*/
    @Override
    public void onDestroy() {
        Logger.log("MyIntentService onDestroy");
        super.onDestroy();
    }
}
