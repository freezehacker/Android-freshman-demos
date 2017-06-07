package com.example.broadcastreceiverdemo01;

import android.app.Application;

/**
 * Created by sjk on 17-6-7.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Logger.log("ApplicationContext: " + this.getApplicationContext());
        Logger.log("Application: " + this);
    }
}
