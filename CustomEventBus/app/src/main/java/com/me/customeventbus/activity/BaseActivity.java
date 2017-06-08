package com.me.customeventbus.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.me.customeventbus.eventbus.EventBus;

/**
 * Created by sjk on 17-6-9.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);   // 注册事件总线
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this); // 反注册，防止活动不能回收导致内存泄漏
        super.onDestroy();
    }
}
