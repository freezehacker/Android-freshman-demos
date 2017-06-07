package com.example.broadcastreceiverdemo01;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class FirstActivity extends AppCompatActivity implements View.OnClickListener {

    // 监听网络连接状态发生改变的广播（属于系统广播）
    public static final String ACTION_NETWORK_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";

    // 自定义广播的名称，一般应该定义在广播的发送者（的类）当中
    public static final String ACTION_CUSTOM_01 = "CUSTOM_BROADCAST_01";
    public static final String ACTION_CUSTOM_02 = "CUSTOM_BROADCAST_02";

    private Button btnSendStandardBroadcast;
    private Button btnSendOrderedBroadcast;

    /*非静态内部类对象持有外部类对象的引用
    即networkChangeReceiver持有firstActivity的一个引用*/
    private BroadcastReceiver networkChangeReceiver;
    private BroadcastReceiver myReceiver;
    private BroadcastReceiver myReceiver2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Logger.log("FirstActivity: " + this);

        setContentView(R.layout.activity_first);
        findViews();
        activateViews();

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_NETWORK_CHANGE);
        networkChangeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Toast.makeText(FirstActivity.this, "Network changed~", Toast.LENGTH_SHORT).show();

                Logger.log("networkChangeReceiver: " + context);

                ConnectivityManager connectivityManager = (ConnectivityManager) FirstActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isAvailable()) {
                    Toast.makeText(FirstActivity.this, "有网络!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FirstActivity.this, "没有网络...", Toast.LENGTH_SHORT).show();
                }
            }
        };
        registerReceiver(networkChangeReceiver, filter);    // 注册系统广播：监听网络连接状态改变

        IntentFilter myFilter = new IntentFilter(FirstActivity.ACTION_CUSTOM_01);
        myReceiver = new MyReceiver();
        registerReceiver(myReceiver, myFilter); // 注册自定义广播

        IntentFilter intentFilter = new IntentFilter(FirstActivity.ACTION_CUSTOM_02);
        intentFilter.setPriority(1000);
        myReceiver2 = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Logger.log("myReceiver2: 收到有序广播");
            }
        };
        registerReceiver(myReceiver2, intentFilter);
    }

    @Override
    protected void onDestroy() {
        // 记得反注册
        unregisterReceiver(networkChangeReceiver);
        unregisterReceiver(myReceiver);

        super.onDestroy();
    }

    /* 通过findViewById取得XML中的控件的引用 */
    private void findViews() {
        btnSendOrderedBroadcast = (Button) findViewById(R.id.send_ordered_broadcast);
        btnSendStandardBroadcast=(Button)findViewById(R.id.send_standard_broadcast);

    }

    /* 赋予控件一些行为、回调 */
    private void activateViews() {
        btnSendOrderedBroadcast.setOnClickListener(this);
        btnSendStandardBroadcast.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_standard_broadcast:
                Intent intent = new Intent();
                intent.setAction(ACTION_CUSTOM_01);
                intent.putExtra("data", "该intent通过广播 传递数据");
                sendBroadcast(intent);
                break;
            case R.id.send_ordered_broadcast:
                Intent i = new Intent(ACTION_CUSTOM_02);
                i.putExtra("data", "我是一个有序广播");
                sendOrderedBroadcast(i, null);
                break;
            default:
        }
    }

    /* 接收器，用来接收自定义广播 */
    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String s = "收到自定义广播：" +
                    "Action: " + intent.getAction() + ", data: " + intent.getStringExtra("data");

            Logger.log(s);
            Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
        }
    }
}
