package com.example.helloworldapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class HelloWorldActivity extends AppCompatActivity {

    private static final String TAG = "HelloWorldTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_world);

        // 有些手机比如魅族，默认logging权限至少为info，所以这一行没有输出
        // 需要进行相应的开发者选项设置，才能够让debug、verbose级别的log正常输出
        Log.d(TAG, "onCreate: hello, debug!");

        // 正常输出
        Log.i(TAG, "onCreate: hello, info");

    }
}
