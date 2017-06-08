package com.example.downloadservicedemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class DownloadService extends Service {

    private String downloadUrl;
    private DownloadTask downloadTask;
    private IDownloadListener downloadListener = new MyDownloadListener();
    private DownloadBinder downloadBinder = new DownloadBinder();

    private static final int NOTIFICATION_ID = 1;

    public DownloadService() {
        Logger.log("DownloadService(): " + this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new DownloadBinder();
    }


    public class DownloadBinder extends Binder {

        public void start(String url) {
            if (downloadTask == null) { // 限制为只能同时存在一个下载任务
                downloadUrl = url;
                downloadTask = new DownloadTask(downloadListener);
                downloadTask.execute(downloadUrl);
            }
        }

        public void cancel() {
            downloadTask.setCancelled(true);
        }

        public void pause() {
            downloadTask.setPaused(true);
        }

        public void resume() {
            //downloadTask.setPaused(false);
            downloadTask = new DownloadTask(downloadListener);
            downloadTask.execute(downloadUrl);
        }
    }

    public class MyDownloadListener implements IDownloadListener {
        long lastUpdated = 0;

        @Override
        public void onProgress(int progress) {
            long curTime = System.currentTimeMillis();
            if (curTime - lastUpdated > 500) {  // 优化2：减少startForeground方法调用次数。减少界面卡顿。
                startForeground(
                        NOTIFICATION_ID,
                        getNotificationBuilder("下载中……", progress + "%").setProgress(100, progress, false).build());
                lastUpdated = curTime;
            }
        }

        @Override
        public void onOK() {
            downloadTask = null;
            stopForeground(true);
            getNotificationManager().notify(
                    NOTIFICATION_ID,
                    getNotificationBuilder("下载完成", "").setTicker("有一项任务已完成").build());
        }

        @Override
        public void onFailed() {
            downloadTask = null;
            stopForeground(true);
            getNotificationManager().notify(
                    NOTIFICATION_ID,
                    getNotificationBuilder("下载失败", "").setTicker("有一项任务已失败").build());
        }

        @Override
        public void onPaused() {
            downloadTask = null;
            Toast.makeText(DownloadService.this, "暂停", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancelled() {
            downloadTask = null;
            stopForeground(true);
            Toast.makeText(DownloadService.this, "下载终止", Toast.LENGTH_SHORT).show();
        }
    }

    private NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private NotificationCompat.Builder getNotificationBuilder(String title, String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        builder.setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentIntent(pi)   // 点击可以跳转到mainActivity
                .setWhen(System.currentTimeMillis());
        return builder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.log("DownloadService onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.log("DownloadService onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Logger.log("[Deprecated]DownloadService onStart");
    }

    @Override
    public void onDestroy() {
        Logger.log("DownloadService onDestroy");
        super.onDestroy();
    }
}
