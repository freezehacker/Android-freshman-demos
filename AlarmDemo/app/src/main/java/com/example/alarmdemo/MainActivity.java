package com.example.alarmdemo;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.trigger).setOnClickListener(v -> {    // P.S. Java1.8支持lambda表达式
            Intent intent = new Intent(this, LongRunningService.class); // P.S. lambda上下文(this)依然是外部对象，不会变为匿名对象，更方便
            startService(intent);

        });
    }
}
