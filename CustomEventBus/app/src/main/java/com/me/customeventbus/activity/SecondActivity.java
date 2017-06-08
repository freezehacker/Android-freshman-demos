package com.me.customeventbus.activity;

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

public class SecondActivity extends BaseActivity implements View.OnClickListener {

    private Button postMyEvent;
    private Button postOtherEvent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        postMyEvent = (Button) findViewById(R.id.post_my_event);
        postOtherEvent = (Button) findViewById(R.id.post_other_event);

        postMyEvent.setOnClickListener(this);
        postOtherEvent.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.post_my_event:
                EventBus.getDefault().post(new MyEvent(200, "OK"));
                break;
            case R.id.post_other_event:
                EventBus.getDefault().post(new OtherEvent("Louis Fonsi"));
                break;
            default:
        }
    }

    @Subscribe
    public void onEvent(OtherEvent event) {
        Logger.log("SecondActivity: " + event);
    }
}
