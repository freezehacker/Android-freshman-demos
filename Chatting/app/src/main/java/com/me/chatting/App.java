package com.me.chatting;

import android.app.Application;
import android.content.Context;

/**
 * Created by sjk on 17-6-10.
 */

public class App extends Application {

    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }
}
