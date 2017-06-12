package com.example.databindingdemo;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.databindingdemo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // 命名为XxxBinding
    // 对应activity_main.xml，生成的类名为ActivityMainBinding
    private ActivityMainBinding binding = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.setUser(new User("square123", "1234567", "http://square.github.io/images/logo.png"));
        binding.setHandler(new MethodReferenceHandler());
        binding.setListener(new ListenerBindingHandler());
    }


    public class MethodReferenceHandler {
        public void clickAlice(View view) {
            LogUtil.log("click Alice");
            binding.setUser(new User("Alice", "alice8919", "http://cdn-img.easyicon.net/png/11859/1185952.gif")); // user引用的实例的地址变了，照样能够自动更新（通知）view
        }

        public void clickBob(View view) {
            binding.setUser(new User("Bob", "bob88834", "http://cdn-img.easyicon.net/png/11859/1185976.gif"));
            LogUtil.log("click Bob");
        }

        public void toSecond(View view) {
            startActivity(new Intent(MainActivity.this, SecondActivity.class));
        }
    }


    public class ListenerBindingHandler {
        public void onPrintUser(User user, View view, long clickAtMillis) {
            LogUtil.log("At " + clickAtMillis + user.toString());
        }
    }
}
