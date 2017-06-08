package com.example.servicedemo;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button startService;
    private Button stopService;

    private Button bindService;
    private Button unbindService;
    private MyService.DownloadBinder myServiceBinder;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 在此取得对Service的（一定程度的）控制权
            myServiceBinder = (MyService.DownloadBinder) service;
            Logger.log("onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Logger.log("onServiceDisconnected");
        }
    };

    private Button startIntentService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        activateViews();

        Logger.log("MainActivity context: " + this);
    }

    private void findViews() {
        startService = (Button) findViewById(R.id.start_service);
        stopService = (Button) findViewById(R.id.stop_service);
        bindService = (Button) findViewById(R.id.bind_service);
        unbindService = (Button) findViewById(R.id.unbind_service);
        startIntentService=(Button) findViewById(R.id.start_intent_service);
    }

    private void activateViews() {
        startService.setOnClickListener(this);
        stopService.setOnClickListener(this);
        bindService.setOnClickListener(this);
        unbindService.setOnClickListener(this);
        startIntentService.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_service:
                startService(new Intent(this, MyService.class));    // 开启服务
                break;
            case R.id.stop_service:
                stopService(new Intent(this, MyService.class)); // 停止服务
                break;
            case R.id.bind_service:
                bindService(
                        new Intent(this, MyService.class),
                        serviceConnection,
                        BIND_AUTO_CREATE);  // 绑定服务
                break;
            case R.id.unbind_service:
                unbindService(serviceConnection);   // 解绑服务
                break;
            case R.id.start_intent_service:
                Logger.log("主线程：" + Thread.currentThread().getId());
                startService(new Intent(this, MyIntentService.class));  // 启动一个IntentService
                break;
            default:
        }
    }
}
