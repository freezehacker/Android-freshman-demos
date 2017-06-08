package com.example.downloadservicedemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button start;
    private Button pauseOrResume;
    private Button stop;
    private Button clearDir;

    private DownloadService.DownloadBinder binder;  // 对Service控制权
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (DownloadService.DownloadBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    private boolean isDownloading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        activateViews();

        bindService(new Intent(this, DownloadService.class), serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);   // 解绑，防止内存泄漏
        super.onDestroy();
    }

    private void findViews() {
        start = (Button) findViewById(R.id.start);
        pauseOrResume = (Button) findViewById(R.id.pause_or_resume);
        stop = (Button) findViewById(R.id.stop);
        clearDir = (Button) findViewById(R.id.clear_dir);
    }

    private void activateViews() {
        start.setOnClickListener(this);

        pauseOrResume.setOnClickListener(this);
        //pauseOrResume.setEnabled(false);
        pauseOrResume.setText("暂停");

        stop.setOnClickListener(this);
        //stop.setEnabled(false);

        clearDir.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                //String url = "http://img.bzdao.com/10834/8835328.jpg";
                String url = "https://raw.githubusercontent.com/guolindev/eclipse/master/eclipse-inst-win64.exe";
                binder.start(url);

                /*start.setEnabled(false);
                pauseOrResume.setEnabled(true);
                stop.setEnabled(true);*/
                isDownloading = true;
                break;
            case R.id.pause_or_resume:
                if (isDownloading) {
                    binder.pause();
                    pauseOrResume.setText("继续");
                } else {
                    binder.resume();
                    pauseOrResume.setText("暂停");
                }

                isDownloading = !isDownloading;
                break;
            case R.id.stop:
                binder.cancel();

                /*start.setEnabled(true);
                stop.setEnabled(false);
                pauseOrResume.setEnabled(false);*/
                break;
            case R.id.clear_dir:
                FileUtils.clearFilesInExtDir();
                break;
            default:
        }
    }
}
