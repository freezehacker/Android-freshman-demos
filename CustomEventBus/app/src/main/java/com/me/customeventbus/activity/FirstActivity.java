package com.me.customeventbus.activity;

import android.app.usage.UsageEvents;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.me.customeventbus.R;
import com.me.customeventbus.eventbus.EventBus;
import com.me.customeventbus.eventbus.Subscribe;

/**
 * Created by sjk on 17-6-8.
 */

public class FirstActivity extends BaseActivity {

    private Button startSecond;
    private boolean isTerminal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        isTerminal = false;

        // 用代码逻辑不断发送事件
        // 每3s发送一次
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isTerminal) {
                    EventBus.getDefault().post(new OtherEvent("Daddy Yankee"));
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        startSecond = (Button) findViewById(R.id.start_second);
        startSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirstActivity.this, SecondActivity.class));
            }
        });
    }

    @Override
    protected void onDestroy() {
        isTerminal = true;
        super.onDestroy();
    }

    @Subscribe
    public void onMyEvent(MyEvent event) {
        Logger.log("FirstActivity收到MyEvent: " + event);
    }

    @Subscribe
    public void onOtherEvent(OtherEvent event) {
        Logger.log("FirstActivity收到OtherEvent: " + event);
    }
}
