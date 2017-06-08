package com.example.servicedemo;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.v4.app.NotificationCompat;

public class MyService extends Service {

    public static final int NOTIFICATION_ID = 1;

    public MyService() {
        Logger.log("MyService Constructor");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Logger.log("MyService onBind");

        return new DownloadBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Logger.log("MyService onUnbind");
        return super.onUnbind(intent);
    }

    /*服务创建时回调*/
    @Override
    public void onCreate() {
        super.onCreate();
        Logger.log("MyService onCreate, context: " + this);

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("title")
                .setContentText("This is a notification. And I am a good man!")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round))
                .setContentIntent(pendingIntent)
                .build();
        startForeground(NOTIFICATION_ID, notification); // 启动一个前台服务
    }

    /*每次服务启动时回调*/
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.log("MyService onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    /*服务销毁时回调*/
    @Override
    public void onDestroy() {
        Logger.log("MyService onDestroy");
        super.onDestroy();
    }

    /*模拟一个后台下载任务*/
    public class DownloadBinder extends Binder {
        public void startDownload() {
            Logger.log("DownloadBinder startDownload");
        }

        public int getProgress() {
            Logger.log("DownloadBinder getProgress");
            return 90;
        }
    }
}
